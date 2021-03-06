package br.com.olist.olistly.service.system;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.olist.olistly.email.exception.AttachmentEmailException;
import br.com.olist.olistly.email.exception.InvalidEmailException;
import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.email.model.Email;
import br.com.olist.olistly.email.service.IEmailService;
import br.com.olist.olistly.exception.DateTimeExpirationException;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.Information;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.repository.system.CredentialRepository;
import br.com.olist.olistly.utilitary.FormatterUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class CredentialService {

	private static final int HOURS_TO_EXPIRATION = 2;

	private Information information;
	private IEmailService emailService;
	private CredentialRepository credentialRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public CredentialService(CredentialRepository credentialRepository, BCryptPasswordEncoder bCryptPasswordEncoder, IEmailService emailService, Information information) {
		this.emailService = emailService;
		this.credentialRepository = credentialRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.information = information;
	}

	public Credential create(Credential credential) throws RequiredFieldException, UniqueFieldException {
		checkRequiredFields(credential);
		checkUniqueFields(credential);
		checkPasswordPattern(credential.getPassword());

		credential.setId(null);
		credential.setPassword(encodePassword(credential.getPassword()));
		return credentialRepository.save(credential);
	}

	public Credential createByPreUser(Credential credential) throws RequiredFieldException, UniqueFieldException {
		checkRequiredFields(credential);
		checkUniqueFields(credential);

		return credentialRepository.save(credential);
	}

	private void checkRequiredFields(Credential credential) {
		if (credential == null)
			throw new RequiredFieldException("A credencial n??o pode ser nulo!");
		if (credential.getUser() == null || ValidatorUtils.isNullOrLessThanOne(credential.getUser().getId()))
			throw new RequiredFieldException("Deve ser informado um usu??rio!");
		if (ValidatorUtils.isNullOrEmpty(credential.getPassword()))
			throw new RequiredFieldException("Deve ser informado o password!");
	}

	private void checkUniqueFields(Credential credential) {
		Long credentialId = ValidatorUtils.isNullOrLessThanOne(credential.getId()) ? null : credential.getId();

		if (existsByUserId(credential.getUser().getId(), credentialId))
			throw new UniqueFieldException("Usu??rio j?? tem credencial cadastrada!");
	}

	public boolean existsByUserId(Long userId, Long id) {
		return credentialRepository.existsByUserId(userId, id);
	}

	public boolean existsByPasswordResetHash(String passwordResetHash) {
		return credentialRepository.existsByPasswordResetHash(passwordResetHash);
	}

	public void checkPasswordPattern(String password) {
		if (ValidatorUtils.isNullOrEmpty(password))
    		throw new RequiredFieldException("Deve ser informado uma senha!");
    	if (password.length() < 8)
    		throw new RequiredFieldException("A senha deve conter no minimo 8 caracteres!");
    	if (!password.matches(".*[0-9].*"))
    		throw new RequiredFieldException("A senha deve conter n??meros!");
    	if (!password.matches(".*[a-zA-Z].*"))
    		throw new RequiredFieldException("A senha deve conter letras!");
	}

	public String encodePassword(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	public Optional<Credential> findByUser(User user) {
		return credentialRepository.findByUser(user);
	}

	public Credential enableConfigurationsToPasswordReset(User user, Credential credential) throws UniqueFieldException, SendEmailException {
		if (credential == null || ValidatorUtils.isNullOrLessThanOne(credential.getId()))
			throw new RequiredFieldException("Deve ser especificado as credenciais do usu??rio que deseja redefinir a senha!");

		if (credential.getPasswordResetExpiration() != null && credential.getPasswordResetExpiration().minusHours(HOURS_TO_EXPIRATION).plusMinutes(5).isAfter(LocalDateTime.now()))
			throw new DateTimeExpirationException("Redefini????o de senha j?? foi solicitada a menos de 5 minutos para esse usu??rio!");

		credential.setPasswordResetHash(generatePasswordResetHash(user, credential));
		credential.setPasswordResetExpiration(LocalDateTime.now().plusHours(HOURS_TO_EXPIRATION));
		updatePasswordResetInformations(credential);

		return credential;
	}

	private String generatePasswordResetHash(User user, Credential credential) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			final String hash = user.getId()+ "." + user.getLogin() + "." + user.getName() + "." + credential.getPasswordResetExpiration() + System.currentTimeMillis();
			final byte[] hashbytes = digest.digest(hash.getBytes(StandardCharsets.UTF_8));

			return FormatterUtils.bytesToHex(hashbytes);
		} catch (NoSuchAlgorithmException exception) {
			throw new RequiredFieldException("Erro ao gerar o hash de redefini????o de senha!");
		}
	}

	public Credential updatePasswordResetInformations(Credential credential) throws RequiredFieldException, UniqueFieldException {
		if (credential == null || ValidatorUtils.isNullOrLessThanOne(credential.getId()))
			throw new RequiredFieldException("Deve ser informado as credencias que deseja atualizar!");

		credentialRepository.updatePasswordResetInformations(credential.getPasswordResetHash(), credential.getPasswordResetExpiration(), credential.getId());
		return credential;
	}

	public void sendPasswordResetEmail(User user, Credential credential) throws SendEmailException {
		if (user == null || ValidatorUtils.isNullOrEmpty(user.getLogin()))
			throw new RequiredFieldException("Deve ser informado o login do usu??rio que deseja enviar o e-mail de redefini????o de senha!");

		if (credential == null || ValidatorUtils.isNullOrEmpty(credential.getPasswordResetHash()))
			throw new RequiredFieldException("Deve ser informado o hash de redefini????o de senha para enviar o e-mail!");

		try {
			sendEmail(user, credential.getPasswordResetHash());
		} catch (AttachmentEmailException | InvalidEmailException | SendEmailException exception) {
			throw new SendEmailException("Erro ao enviar e-mail de redefini????o de senha do usu??rio! ", exception);
		}
	}

	private void sendEmail(User user, String hash) {
		emailService.send(new Email()
							.from(information.getNoReply())
							.subject("Redefini????o de senha do usu??rio")
							.addTo(user.getLogin())
							.messageHTML(getMessageText(user, hash))
							.messageHTML(getMessageHTML(user, hash))
						);
	}

	private String getMessageText(User user, String hash) {
		return "Ol?? "+user.getName()+"!\n"+
				"Voc?? solicitou a redefini????o de senha para acesso ao "+information.getName()+", para redefini-la acesse o link abaixo:\n"+
				information.getFrontendURL()+"/redefinirsenha/"+hash+"\n\n\n"+
				"Caso n??o tenha solicitado esse e-mail, basta ignor??-lo\n\n"+
				"Att,\n"+
				"Equipe "+information.getName()+"\n"+
				information.getHomepage()+" \n"+
				"# ESTA ?? UMA MENSAGEM AUTOM??TICA \n\n"+
				"# N??O ?? NECESS??RIO RESPOND??-LA \n";
	}

	private String getMessageHTML(User user, String hash) {
		return "Ol?? <b>"+user.getName()+"</b>!<br/><br/>"+
				"Voc?? solicitou a redefini????o de senha para acesso ao "+information.getName()+", para redefini-la acesse o link abaixo: <br/>"+
				"<a href='"+information.getFrontendURL()+"/redefinirsenha/"+hash+"'>"+information.getFrontendURL()+"/redefinirsenha/"+hash+"</a><br/><br/> "+
				"Caso n??o tenha solicitado esse e-mail, basta ignor??-lo.<br/><br/>"+
				"<b>Att,</b><br/>"+
				"<b>Equipe "+information.getName()+"</b><br/>"+
				"<a href='"+information.getHomepage()+"'>"+information.getHomepage()+"</a><br/><br/>"+
				"# ESTA ?? UMA MENSAGEM AUTOM??TICA<br/>"+
				"# N??O ?? NECESS??RIO RESPOND??-LA";
	}

	public void passwordChange(Credential credential, String currentPassword, String newPassword) {
		if (ValidatorUtils.isNullOrEmpty(currentPassword))
    		throw new RequiredFieldException("Deve ser informado a senha atual!");

    	if (ValidatorUtils.isNullOrEmpty(newPassword))
    		throw new RequiredFieldException("Deve ser informado a nova senha!");

    	checkPasswordPattern(newPassword);

    	if (!bCryptPasswordEncoder.matches(currentPassword, credential.getPassword()))
    		throw new RequiredFieldException("A senha atual n??o esta correta!");

        credential.setPassword(encodePassword(newPassword));

        credentialRepository.passwordChange(credential.getId(), credential.getPassword());
	}

	public void passwordReset(User user, String passwordResetHash, String newPassword) {
		if (ValidatorUtils.isNullOrEmpty(passwordResetHash))
    		throw new RequiredFieldException("Deve ser informado a hash de redefini????o de senha!");

    	if (ValidatorUtils.isNullOrEmpty(newPassword))
    		throw new RequiredFieldException("Deve ser informado a nova senha!");

    	Optional<Credential> credential = findByUser(user);

		if (!credential.isPresent())
			throw new RequiredFieldException("Credenciais do usu??rio s??o invalidas!");

    	if (!passwordResetHash.equals(credential.get().getPasswordResetHash()))
    		throw new RequiredFieldException("Deve ser informado um hash v??lido para esse usu??rio!");

    	if (LocalDateTime.now().isAfter(credential.get().getPasswordResetExpiration()))
    		throw new DateTimeExpirationException("O tempo para redefinir a senha j?? expirou, tente solicitar novamente a redefini????o de senha!");

    	checkPasswordPattern(newPassword);

        credential.get().setPassword(encodePassword(newPassword));
        credentialRepository.passwordChange(credential.get().getId(), credential.get().getPassword());
	}
}
