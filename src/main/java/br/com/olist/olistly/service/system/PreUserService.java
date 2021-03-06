package br.com.olist.olistly.service.system;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.olist.olistly.email.exception.AttachmentEmailException;
import br.com.olist.olistly.email.exception.InvalidEmailException;
import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.email.model.Email;
import br.com.olist.olistly.email.service.IEmailService;
import br.com.olist.olistly.exception.DateTimeExpirationException;
import br.com.olist.olistly.exception.RecordNotFoundException;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.model.system.Information;
import br.com.olist.olistly.model.system.PreUser;
import br.com.olist.olistly.repository.system.PreUserRepository;
import br.com.olist.olistly.utilitary.FormatterUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class PreUserService {

	private static final int HOURS_TO_EXPIRATION = 24;

	private PreUserRepository preUserRepository;
	private CredentialService credentialService;
	private IEmailService emailService;
	private UserService userService;
	private Information information;

	public PreUserService(PreUserRepository preUserRepository, UserService userService, CredentialService credentialService, IEmailService emailService, Information information) {
		this.preUserRepository = preUserRepository;
		this.credentialService = credentialService;
		this.emailService = emailService;
		this.userService = userService;
		this.information = information;
	}

	public PreUser create(PreUser preUser) throws RequiredFieldException, UniqueFieldException, SendEmailException {
		checkRequiredFields(preUser);
		checkUniqueFields(preUser);

		credentialService.checkPasswordPattern(preUser.getPassword());

		preUser.setId(null);
		preUser.setLogin(preUser.getLogin().toLowerCase());
		preUser.setPassword(credentialService.encodePassword(preUser.getPassword()));
		preUser.setDateExpiration(LocalDateTime.now().plusHours(HOURS_TO_EXPIRATION));
		preUser.setActivationHash(generateActivationHash(preUser));
		preUserRepository.save(preUser);

		try {
			sendEmail(preUser);
			return preUser;
		} catch (AttachmentEmailException | InvalidEmailException | SendEmailException exception) {
			preUserRepository.delete(preUser);
			throw new SendEmailException("Erro ao enviar e-mail de confirma????o de usu??rio! ", exception);
		}
	}

	public PreUser resendActivetionHashAndUpdateDateExpiration(String login) throws SendEmailException {
		if (ValidatorUtils.isNullOrEmpty(login))
			throw new RecordNotFoundException("Pr?? usu??rio n??o encontrado!");

		Optional<PreUser> preUser = preUserRepository.findByLogin(login);

		if (!preUser.isPresent())
			throw new RecordNotFoundException("Pr?? usu??rio n??o encontrado!");

		if (preUser.get().getDateExpiration().minusHours(HOURS_TO_EXPIRATION).plusMinutes(5).isAfter(LocalDateTime.now()))
			throw new DateTimeExpirationException("Um e-mail de confirma????o j?? foi enviado a menos de 5 minutos para esse endere??o!");

		try {
			sendEmail(preUser.get());
			preUser.get().setDateExpiration(LocalDateTime.now().plusHours(HOURS_TO_EXPIRATION));
			preUserRepository.save(preUser.get());
			return preUser.get();
		} catch (AttachmentEmailException | InvalidEmailException | SendEmailException exception) {
			throw new SendEmailException("Erro ao enviar e-mail de confirma????o de usu??rio! ", exception);
		}
	}

	private void checkRequiredFields(PreUser preUser) {
		if (Objects.isNull(preUser))
			throw new RequiredFieldException("O pr?? usu??rio n??o pode ser nullo!");
		if (ValidatorUtils.isNullOrEmpty(preUser.getLogin()))
			throw new RequiredFieldException("Deve ser informado o login do pr?? usu??rio!");
		if (ValidatorUtils.isNullOrEmpty(preUser.getName()))
			throw new RequiredFieldException("Deve ser informado o nome do pr?? usu??rio!");
		if (!ValidatorUtils.isValidEmail(preUser.getLogin()))
			throw new RequiredFieldException("Deve ser informado um e-mail valido para o login!");
	}

	private void checkUniqueFields(PreUser preUser) {
		Long preUserId = ValidatorUtils.isNullOrLessThanOne(preUser.getId()) ? null : preUser.getId();

		if (userService.existsByLogin(preUser.getLogin(), null) || preUserRepository.existsByLogin(preUser.getLogin(), preUserId))
			throw new UniqueFieldException("O Login informado j?? existe!");
	}

	private String generateActivationHash(PreUser preUser) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			final String hash = preUser.getLogin() + "." + preUser.getName() + "." + preUser.getPassword() + "." + preUser.getDateExpiration() + "." + System.currentTimeMillis();
			final byte[] hashbytes = digest.digest(hash.getBytes(StandardCharsets.UTF_8));

			return FormatterUtils.bytesToHex(hashbytes);
		} catch (NoSuchAlgorithmException exception) {
			throw new RequiredFieldException("Erro ao gerar o hash de ativa????o do pr?? usu??rio!");
		}
	}

	public boolean existsByLogin(String login) {
		if (ValidatorUtils.isNullOrEmpty(login))
			return true;

		return preUserRepository.existsByLogin(login, null);
	}

	public Optional<PreUser> findByActivationHash(String hash) {
		return preUserRepository.findByActivationHash(hash);
	}

	public void deleteById(Long id) {
		if (!ValidatorUtils.isNullOrLessThanOne(id))
			preUserRepository.deleteById(id);
	}

	private void sendEmail(PreUser preUser) {
		emailService.send(new Email()
							.from(information.getNoReply())
							.subject("Confirma????o de usu??rio")
							.addTo(preUser.getLogin())
							.messageHTML(getMessageText(preUser))
							.messageHTML(getMessageHTML(preUser))
						);
	}

	private String getMessageText(PreUser preUser) {
		return "Ol?? "+preUser.getName()+"!\n"+
				"Voc?? solicitou um novo acesso ao "+information.getName()+", para ativ??-lo acesse o link abaixo:\n"+
				information.getFrontendURL()+"/ativar/"+preUser.getActivationHash()+"\n\n\n"+
				"Caso n??o tenha solicitado esse e-mail, basta ignor??-lo\n\n"+
				"Att,\n"+
				"Equipe "+information.getName()+"\n"+
				information.getHomepage()+" \n"+
				"# ESTA ?? UMA MENSAGEM AUTOM??TICA \n\n"+
				"# N??O ?? NECESS??RIO RESPOND??-LA \n";
	}

	private String getMessageHTML(PreUser preUser) {
		return "Ol?? <b>"+preUser.getName()+"</b>!<br/><br/>"+
				"Voc?? solicitou um novo acesso ao "+information.getName()+", para ativ??-lo clique no link abaixo: <br/>"+
				"<a href='"+information.getFrontendURL()+"/ativar/"+preUser.getActivationHash()+"'>"+information.getFrontendURL()+"/ativar/"+preUser.getActivationHash()+"</a><br/><br/> "+
				"Caso n??o tenha solicitado esse e-mail, basta ignor??-lo.<br/><br/>"+
				"<b>Att,</b><br/>"+
				"<b>Equipe "+information.getName()+"</b><br/>"+
				"<a href='"+information.getHomepage()+"'>"+information.getHomepage()+"</a><br/><br/>"+
				"# ESTA ?? UMA MENSAGEM AUTOM??TICA<br/>"+
				"# N??O ?? NECESS??RIO RESPOND??-LA";
	}
}
