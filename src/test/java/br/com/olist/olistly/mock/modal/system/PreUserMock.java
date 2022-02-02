package br.com.olist.olistly.mock.modal.system;

import java.time.LocalDateTime;

import br.com.olist.olistly.model.system.PreUser;

public class PreUserMock {
	private static final Long IDMOCK = Long.valueOf(1);
	private static final String LOGIN = "nothing@valid.com";
	private static final String NAME = "noname";
	private static final String PASSWORD = "superpassword";

	public static PreUser mock() {
		PreUser preUser = new PreUser();
		preUser.setId(IDMOCK);
		preUser.setLogin(LOGIN);
		preUser.setName(NAME);
		preUser.setPassword(PASSWORD);
		preUser.setDateExpiration(LocalDateTime.of(2023, 01, 31, 23, 59, 59));
		preUser.setActivationHash("superactivehash");
		return preUser;
	}
}
