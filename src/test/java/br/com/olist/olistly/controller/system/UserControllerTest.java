package br.com.olist.olistly.controller.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.olist.olistly.WrapperConfiguration;
import br.com.olist.olistly.helper.WrapperHelper;
import br.com.olist.olistly.mock.modal.system.SessionMock;
import br.com.olist.olistly.mock.security.WithMockCustomUser;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.service.system.SessionService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WrapperConfiguration.class)
public class UserControllerTest {

	@Autowired
	private MockMvc controller;

	@Autowired
	WrapperHelper wrapperHelper;

	@MockBean
	private SessionService sessionService;

	private Session session;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		session = SessionMock.mock();
	}

	@Test
	@WithMockCustomUser
	public void info_logged_user() throws Exception {
		MvcResult response = this.controller
				.perform(get("/user/info"))
				.andExpect(status().isOk())
				.andReturn();

		verify(sessionService, atLeastOnce()).save(any());
		User returnedUser = wrapperHelper.unwrapper(response.getResponse().getContentAsString(), User.class);

		assertEquals(session.getUser().getId(), returnedUser.getId());
		assertEquals(session.getUser().getLogin(), returnedUser.getLogin());
		assertEquals(session.getUser().getName(), returnedUser.getName());

	}
}
