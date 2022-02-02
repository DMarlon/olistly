package br.com.olist.olistly.controller.unauthenticated.system;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.olist.olistly.dto.system.PasswordResetDTO;
import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.exception.DateTimeExpirationException;
import br.com.olist.olistly.exception.RecordNotFoundException;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.model.system.PreUser;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.service.system.PreUserService;
import br.com.olist.olistly.service.system.UserService;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@RestController("UnauthenticatedUserController")
@RequestMapping("/public/user")
public class UserController {

	private UserService userService;
	private PreUserService preUserService;

	public UserController(UserService userService, PreUserService preUserService) {
		this.userService = userService;
		this.preUserService = preUserService;
	}

	@GetMapping("/activate/{hash:[A-Z0-9]{40}}")
	public User activation(@PathVariable("hash") String hash) {
		Optional<PreUser> preUser = preUserService.findByActivationHash(hash);

		if (!preUser.isPresent())
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);

		if (LocalDateTime.now().isAfter(preUser.get().getDateExpiration())) {
			preUserService.deleteById(preUser.get().getId());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tempo para ativação já expirou, cadastre-se novamente!");
		}

		try {
			User user = userService.createByPreUser(preUser.get());
			preUserService.deleteById(preUser.get().getId());
			return user;
		} catch (RequiredFieldException | UniqueFieldException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar usuário, contate o suporte!");
		}
	}

	@PostMapping("/create")
	public PreUser sigin(@RequestBody PreUser preUser) {
		if (!ValidatorUtils.isValidEmail(preUser.getLogin()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado um e-mail valido para o login!");

		try {
			if (preUserService.existsByLogin(preUser.getLogin()))
				return preUserService.resendActivetionHashAndUpdateDateExpiration(preUser.getLogin());
			else
				return preUserService.create(preUser);
		} catch (RecordNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (RequiredFieldException | UniqueFieldException | DateTimeExpirationException | SendEmailException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar usuário, contate o suporte!");
		}
	}

	@GetMapping("/resetpassword/{hash:[A-Z0-9]{40}}")
	public ResponseEntity<Void> existsByPasswordResetHash(@PathVariable("hash") String passwordResetHash) {
		if (userService.existsByPasswordResetHash(passwordResetHash))
			return new ResponseEntity<Void>(HttpStatus.OK);
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/resetpassword")
	public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetDTO passwordResetDTO) {
		if (!ValidatorUtils.isValidEmail(passwordResetDTO.getLogin()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado um e-mail válido para redefinir a senha!");

		try {
			User user = userService.findByLogin(passwordResetDTO.getLogin());

			if (ValidatorUtils.isNullOrLessThanOne(user.getId()))
				throw new ResponseStatusException(HttpStatus.NO_CONTENT);

			userService.requestPasswordReset(user);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (RecordNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (RequiredFieldException | DateTimeExpirationException | SendEmailException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao redefinir senha do usuário, contate o suporte!");
		}
	}

	@PutMapping("/resetpassword/{hash:[A-Z0-9]{40}}")
	public ResponseEntity<Void> passwordReset(@PathVariable("hash") String passwordResetHash, @RequestBody PasswordResetDTO passwordResetDTO) {
		if (!ValidatorUtils.isValidEmail(passwordResetDTO.getLogin()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado um e-mail válido para redefinir a senha!");

		try {
			User user = userService.findByLogin(passwordResetDTO.getLogin());

			if (ValidatorUtils.isNullOrLessThanOne(user.getId()))
				throw new ResponseStatusException(HttpStatus.NO_CONTENT);

			userService.passwordReset(user, passwordResetHash, passwordResetDTO.getPassword());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (RecordNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (RequiredFieldException | DateTimeExpirationException | SendEmailException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao redefinir senha do usuário, contate o suporte!");
		}
	}
}
