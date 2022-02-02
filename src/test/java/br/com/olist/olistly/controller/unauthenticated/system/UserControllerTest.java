package br.com.olist.olistly.controller.unauthenticated.system;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.olist.olistly.WrapperConfiguration;
import br.com.olist.olistly.dto.system.PasswordResetDTO;
import br.com.olist.olistly.helper.WrapperHelper;
import br.com.olist.olistly.mock.dto.system.PasswordResetDTOMock;
import br.com.olist.olistly.mock.modal.system.PreUserMock;
import br.com.olist.olistly.mock.modal.system.UserMock;
import br.com.olist.olistly.model.system.PreUser;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.service.system.PreUserService;
import br.com.olist.olistly.service.system.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WrapperConfiguration.class)
public class UserControllerTest {
	private static final String HASH = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	@Autowired
	private MockMvc controller;

	@Autowired
	private WrapperHelper wrapperHelper;

	@MockBean
	private UserService userService;

	@MockBean
	private PreUserService preUserService;

	private User user;
	private PreUser preUser;
	private PasswordResetDTO passwordResetDTO;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		user = UserMock.mock();
		preUser = PreUserMock.mock();
		passwordResetDTO = PasswordResetDTOMock.mock();
	}

	@Test
	public void activation_invalid_hash() throws Exception {
		when(preUserService.findByActivationHash(anyString())).thenReturn(Optional.empty());

		this.controller
				.perform(get("/public/user/activate/"+HASH))
				.andExpect(status().isNoContent());
	}

	@Test
	public void activation_recently_sent() throws Exception {
		preUser.setDateExpiration(LocalDateTime.now().minusHours(1));
		when(preUserService.findByActivationHash(anyString())).thenReturn(Optional.of(preUser));

		this.controller
				.perform(get("/public/user/activate/"+HASH))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("O tempo para ativação já expirou, cadastre-se novamente!")));
	}

	@Test
	public void activation_sucess() throws Exception {
		when(preUserService.findByActivationHash(anyString())).thenReturn(Optional.of(preUser));
		when(userService.createByPreUser(preUser)).thenReturn(user);

		MvcResult response = this.controller
				.perform(get("/public/user/activate/"+HASH))
				.andExpect(status().isOk())
				.andReturn();

		verify(preUserService, atLeastOnce()).deleteById(preUser.getId());

		 User returnedUser = wrapperHelper.unwrapper(response.getResponse().getContentAsString(), User.class);
		 assertEquals(user.getId(), returnedUser.getId());
		 assertEquals(user.getLogin(), returnedUser.getLogin());
		 assertEquals(user.getName(), returnedUser.getName());
	}

	@Test
	public void create_invalid_login() throws Exception {
		preUser.setLogin("invalid");

		this.controller.perform(
					post("/public/user/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(preUser))
				)
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Deve ser informado um e-mail valido para o login!")));
	}

	@Test
	public void create_existent_login() throws Exception {
		when(preUserService.existsByLogin(user.getLogin())).thenReturn(true);

		this.controller.perform(
					post("/public/user/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(preUser))
				)
				.andExpect(status().isOk());

		verify(preUserService, atLeastOnce()).resendActivetionHashAndUpdateDateExpiration(preUser.getLogin());
		verify(preUserService, never()).create(any());
	}

	@Test
	public void create_non_existent_login() throws Exception {
		when(preUserService.existsByLogin(user.getLogin())).thenReturn(false);

		this.controller.perform(
					post("/public/user/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(preUser))
				)
				.andExpect(status().isOk());

		verify(preUserService, never()).resendActivetionHashAndUpdateDateExpiration(preUser.getLogin());
		verify(preUserService, atLeastOnce()).create(any());
	}

	@Test
	public void resetpassword_invalid_hash() throws Exception {
		this.controller
				.perform(get("/public/user/resetpassword/"+HASH))
				.andExpect(status().isNoContent());
	}

	@Test
	public void resetpassword_valid_hash() throws Exception {
		when(userService.existsByPasswordResetHash(anyString())).thenReturn(true);

		this.controller
				.perform(get("/public/user/resetpassword/"+HASH))
				.andExpect(status().isOk());
	}

	@Test
	public void resetpassword_invalid_login() throws Exception {
		passwordResetDTO.setLogin("invalid");

		this.controller.perform(
					post("/public/user/resetpassword")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Deve ser informado um e-mail válido para redefinir a senha!")));
	}

	@Test
	public void resetpassword_notfound_user() throws Exception {
		when(userService.findByLogin(passwordResetDTO.getLogin())).thenReturn(new User());

		this.controller.perform(
					post("/public/user/resetpassword")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isInternalServerError())
				.andExpect(status().reason(containsString("Erro ao redefinir senha do usuário, contate o suporte!")));

		verify(userService, never()).requestPasswordReset(any());
	}

	@Test
	public void resetpassword_valid() throws Exception {
		when(userService.findByLogin(passwordResetDTO.getLogin())).thenReturn(user);

		this.controller.perform(
					post("/public/user/resetpassword")
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isOk());

		verify(userService, atLeastOnce()).requestPasswordReset(user);
	}

	@Test
	public void resetpassword_put_invalid_login() throws Exception {
		passwordResetDTO.setLogin("invalid");

		this.controller.perform(
					put("/public/user/resetpassword/"+HASH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Deve ser informado um e-mail válido para redefinir a senha!")));
	}

	@Test
	public void resetpassword_put_notfound_user() throws Exception {
		when(userService.findByLogin(passwordResetDTO.getLogin())).thenReturn(new User());

		this.controller.perform(
					put("/public/user/resetpassword/"+HASH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isInternalServerError())
				.andExpect(status().reason(containsString("Erro ao redefinir senha do usuário, contate o suporte!")));

		verify(userService, never()).requestPasswordReset(any());
	}

	@Test
	public void resetpassword_put_valid() throws Exception {
		when(userService.findByLogin(passwordResetDTO.getLogin())).thenReturn(user);

		this.controller.perform(
					put("/public/user/resetpassword/"+HASH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(wrapperHelper.wrapper(passwordResetDTO))
				)
				.andExpect(status().isOk());

		verify(userService, atLeastOnce()).passwordReset(user, HASH, passwordResetDTO.getPassword());
	}
}
