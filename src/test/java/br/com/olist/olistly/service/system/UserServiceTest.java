package br.com.olist.olistly.service.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.mock.modal.system.CredentialMock;
import br.com.olist.olistly.mock.modal.system.PreUserMock;
import br.com.olist.olistly.mock.modal.system.UserMock;
import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.PreUser;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.repository.system.UserRepository;

@SpringBootTest
public class UserServiceTest {
	private static final String HASH = "AAAAAAAAAA";

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SessionService sessionService;

	@MockBean
	private CredentialService credentialService;

	private UserService userService;

	private User user;
	private PreUser preUser;
	private Credential credential;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);

		user = UserMock.mock();
		preUser = PreUserMock.mock();
		credential = CredentialMock.mock();

		when(credentialService.existsByPasswordResetHash(anyString())).thenReturn(true);
		when(credentialService.createByPreUser(any())).thenReturn(credential);
		when(credentialService.findByUser(any())).thenReturn(Optional.of(credential));

		userService = new UserService(userRepository, sessionService, credentialService);
	}

	@Test
	public void findByLogin_with_null() {
		User user = userService.findByLogin(null);
		verify(userRepository, never()).findByLogin(null);
		assertNull(user.getId());
	}

	@Test
	public void findByLogin_with_empty() {
		User user = userService.findByLogin("");
		verify(userRepository, never()).findByLogin("");
		assertNull(user.getId());
	}

	@Test
	public void findByLogin_with_valid() {
		when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
		User userFound = userService.findByLogin(user.getLogin());
		assertEquals(user.getId(), userFound.getId());
	}

	@Test
	public void createByPreUser_with_value() {
		User userCreated = userService.createByPreUser(preUser);
		verify(userRepository, atLeastOnce()).existsByLogin(anyString(), any());
		verify(credentialService, atLeastOnce()).createByPreUser(any());

		assertEquals(user.getLogin(), userCreated.getLogin());
		assertEquals(user.getName(), userCreated.getName());
		assertEquals(Status.ENABLE, userCreated.getStatus());
	}

	@Test
	public void createByPreUser_with_login_null() {
		preUser.setLogin(null);
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.createByPreUser(preUser); });
 		assertEquals("Deve ser informado o login do usuário!", exception.getMessage());
	}

	@Test
	public void createByPreUser_with_login_empty() {
		preUser.setLogin("");
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.createByPreUser(preUser); });
 		assertEquals("Deve ser informado o login do usuário!", exception.getMessage());
	}

	@Test
	public void createByPreUser_with_name_null() {
		preUser.setName(null);
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.createByPreUser(preUser); });
 		assertEquals("Deve ser informado o nome do usuário!", exception.getMessage());
	}

	@Test
	public void createByPreUser_with_name_empty() {
		preUser.setName("");
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.createByPreUser(preUser); });
 		assertEquals("Deve ser informado o nome do usuário!", exception.getMessage());
	}

	@Test
	public void createByPreUser_with_existsuser() {
		when(userRepository.existsByLogin(user.getLogin(), null)).thenReturn(true);

		UniqueFieldException exception = assertThrows(UniqueFieldException.class, () -> { userService.createByPreUser(preUser); });
 		assertEquals("O Login informado já existe!", exception.getMessage());
	}

	@Test
	public void existsByPasswordResetHash_with_null() {
		Boolean exists = userService.existsByPasswordResetHash(null);
		verify(credentialService, never()).existsByPasswordResetHash(null);
		assertFalse(exists);
	}

	@Test
	public void existsByPasswordResetHash_with_empty() {
		Boolean exists = userService.existsByPasswordResetHash("");
		verify(credentialService, never()).existsByPasswordResetHash(anyString());
		assertFalse(exists);
	}

	@Test
	public void existsByPasswordResetHash_with_value() {
		Boolean exists = userService.existsByPasswordResetHash(HASH);
		verify(credentialService, atLeastOnce()).existsByPasswordResetHash(HASH);
		assertTrue(exists);
	}

	@Test
	public void existsByLogin_with_null() {
		boolean exists = userService.existsByLogin(null, Long.valueOf(1));
		verify(userRepository, never()).existsByLogin(anyString(), anyLong());
		assertTrue(exists);
	}

	@Test
	public void existsByLogin_with_empty() {
		boolean exists = userService.existsByLogin("", Long.valueOf(1));
		verify(userRepository, never()).existsByLogin(anyString(), anyLong());
		assertTrue(exists);
	}

	@Test
	public void existsByLogin_with_trim() {
		when(userRepository.existsByLogin(user.getLogin(), Long.valueOf(1))).thenReturn(true);
		boolean exists = userService.existsByLogin(" "+user.getLogin()+" ", Long.valueOf(1));
		assertTrue(exists);
	}

	@Test
	public void requestPasswordReset_with_null() {
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.requestPasswordReset(null); });
		assertEquals("Deve ser informado um usuário para redefinição de senha!", exception.getMessage());
	}

	@Test
	public void requestPasswordReset_with_idLessThanOne() {
		user.setId(Long.valueOf(-100));

		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.requestPasswordReset(user); });
		assertEquals("Deve ser informado um usuário para redefinição de senha!", exception.getMessage());
	}

	@Test
	public void requestPasswordReset_without_credential() {
		when(credentialService.findByUser(any())).thenReturn(Optional.empty());
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.requestPasswordReset(user); });
		assertEquals("Credencias do usuário são invalidas!", exception.getMessage());
	}

	@Test
	public void requestPasswordReset_with_value() {
		userService.requestPasswordReset(user);
		verify(credentialService, atLeastOnce()).findByUser(user);
		verify(credentialService, atLeastOnce()).enableConfigurationsToPasswordReset(user, credential);
		verify(credentialService, atLeastOnce()).sendPasswordResetEmail(user, credential);
	}

	@Test
	public void passwordReset_with_null() {
		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.passwordReset(null, "", ""); });
		assertEquals("Deve ser informado um usuário para redefinição de senha!", exception.getMessage());
	}

	@Test
	public void passwordReset_with_idLessThanOne() {
		user.setId(Long.valueOf(-100));

		RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> { userService.passwordReset(user, "", ""); });
		assertEquals("Deve ser informado um usuário para redefinição de senha!", exception.getMessage());
	}

	@Test
	public void passwordReset_with_value() {
		userService.passwordReset(user, HASH, credential.getPassword());
		verify(credentialService, atLeastOnce()).passwordReset(user, HASH, credential.getPassword());
		verify(sessionService, atLeastOnce()).removeAll(user);
	}
}
