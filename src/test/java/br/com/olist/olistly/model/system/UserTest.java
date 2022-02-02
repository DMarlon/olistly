package br.com.olist.olistly.model.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
	private static final String LOGIN = "suporte@phbit.com.br";
	private static final String NAME = "Phbit company";

	User user_null;
	User user_trim;

	@BeforeEach
	public void init() {
		user_null = new User();
		user_null.setLogin(null);
		user_null.setName(null);

		user_trim = new User();
		user_trim.setLogin(" "+LOGIN+" ");
		user_trim.setName(" "+NAME+" ");
	}

	@Test
	public void setLogin_null_value() {
		assertNull(user_null.getLogin());
	}

	@Test
	public void setName_null_value() {
		assertNull(user_null.getName());
	}

	@Test
	public void setLogin_trim_value() {
		assertEquals(user_trim.getLogin(), LOGIN);
	}

	@Test
	public void setName_trim_value() {
		assertEquals(user_trim.getName(), NAME);
	}
}
