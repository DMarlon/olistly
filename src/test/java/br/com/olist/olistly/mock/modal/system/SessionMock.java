package br.com.olist.olistly.mock.modal.system;

import br.com.olist.olistly.model.system.Session;

public class SessionMock {
	private static final Long IDMOCK = Long.valueOf(1);

	public static Session mock() {
		Session session = new Session();
		session.setId(IDMOCK);
		session.setUser(UserMock.mock());
		return session;
	}

}
