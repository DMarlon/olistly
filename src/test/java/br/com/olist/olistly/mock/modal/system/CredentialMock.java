package br.com.olist.olistly.mock.modal.system;

import java.time.LocalDateTime;

import br.com.olist.olistly.model.system.Credential;

public class CredentialMock {
	private static final Long IDMOCK = Long.valueOf(1);
	private static final String HASH = "superhash";
	private static final String PASSWORD = "superpassword";

	public static Credential mock() {
		Credential credential = new Credential();
		credential.setId(IDMOCK);
		credential.setPassword(PASSWORD);
		credential.setPasswordResetHash(HASH);
		credential.setPasswordResetExpiration(LocalDateTime.of(2022, 01, 31, 23, 59, 59));
		return credential;
	}
}
