package br.com.olist.olistly.service.system;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.exception.DateTimeExpirationException;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.PreUser;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.repository.system.UserRepository;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class UserService {

	private UserRepository userRepository;
	private SessionService sessionService;
	private CredentialService credentialService;

	public UserService(UserRepository userRepository, SessionService sessionService, CredentialService credentialService) {
		this.userRepository = userRepository;
		this.sessionService = sessionService;
		this.credentialService = credentialService;
	}

	public User findByLogin(String login) {
		if (ValidatorUtils.isNullOrEmpty(login))
			return new User();

		Optional<User> user = userRepository.findByLogin(login.toLowerCase());
		return user.orElse(new User());
	}

	public User createByPreUser(PreUser preUser) throws RequiredFieldException, UniqueFieldException {
		User user = new User();
		user.setLogin(preUser.getLogin());
		user.setName(preUser.getName());
		user.setStatus(Status.ENABLE);

		checkRequiredFields(user);
		checkUniqueFields(user);

		userRepository.save(user);

		Credential credential = new Credential();
		credential.setUser(user);
		credential.setPassword(preUser.getPassword());
		credentialService.createByPreUser(credential);

		return user;
	}

	private void checkRequiredFields(User user) {
		if (Objects.isNull(user))
			throw new RequiredFieldException("O usuário não pode ser nulo!");
		if (ValidatorUtils.isNullOrEmpty(user.getLogin()))
			throw new RequiredFieldException("Deve ser informado o login do usuário!");
		if (ValidatorUtils.isNullOrEmpty(user.getName()))
			throw new RequiredFieldException("Deve ser informado o nome do usuário!");
		if (Objects.isNull(user.getStatus()))
			throw new RequiredFieldException("Deve ser informado a situação do usuário!");
	}

	private void checkUniqueFields(User user) {
		Long userId = ValidatorUtils.isNullOrLessThanOne(user.getId()) ? null : user.getId();

		if (existsByLogin(user.getLogin(), userId))
			throw new UniqueFieldException("O Login informado já existe!");
	}

	public boolean existsByPasswordResetHash(String passwordResetHash) {
		if (ValidatorUtils.isNullOrEmpty(passwordResetHash))
			return false;

		return credentialService.existsByPasswordResetHash(passwordResetHash);
	}

	public boolean existsByLogin(String login, Long id) {
		if (ValidatorUtils.isNullOrEmpty(login))
			return true;

		return userRepository.existsByLogin(login.trim(), id);
	}

	public void requestPasswordReset(User user) throws RequiredFieldException, DateTimeExpirationException, SendEmailException {
		if (user == null || ValidatorUtils.isNullOrLessThanOne(user.getId()))
			throw new RequiredFieldException("Deve ser informado um usuário para redefinição de senha!");

		Optional<Credential> credential = credentialService.findByUser(user);

		if (!credential.isPresent())
			throw new RequiredFieldException("Credencias do usuário são invalidas!");

		user.setLogin(user.getLogin());
		credentialService.enableConfigurationsToPasswordReset(user, credential.get());
		credentialService.sendPasswordResetEmail(user, credential.get());
	}

	public void passwordReset(User user, String passwordResetHash, String newPassword) throws RequiredFieldException, DateTimeExpirationException, SendEmailException {
		if (user == null || ValidatorUtils.isNullOrLessThanOne(user.getId()))
			throw new RequiredFieldException("Deve ser informado um usuário para redefinição de senha!");

		user.setLogin(user.getLogin());

		credentialService.passwordReset(user, passwordResetHash, newPassword);
		sessionService.removeAll(user);
	}
}
