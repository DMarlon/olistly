package br.com.olist.olistly.mock.modal.system;

import br.com.olist.olistly.model.system.User;

public class UserMock {
	private static final Long IDMOCK = Long.valueOf(1);
	private static final String LOGIN = "nothing@valid.com";
	private static final String NAME = "noname";

	public static User mock() {
		User user = new User();
		user.setId(IDMOCK);
		user.setLogin(LOGIN);
		user.setName(NAME);
		return user;
	}
}
