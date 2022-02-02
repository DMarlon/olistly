package br.com.olist.olistly.model.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CredentialTest {

	private static final String HASH = "hash123";
	private static final String PASSWORD = "deviaserhash";

	Credential credential_null;
	Credential credential_trim;

	@BeforeEach
	public void init() {
		credential_null = new Credential();
		credential_null.setPassword(null);
		credential_null.setPasswordResetHash(null);

		credential_trim = new Credential();
		credential_trim.setPassword(" "+PASSWORD+" ");
		credential_trim.setPasswordResetHash(" "+HASH+" ");
	}

	@Test
	public void setPassword_null_value() {
		assertNull(credential_null.getPassword());
	}

	@Test
	public void setPasswordResetHash_null_value() {
		assertNull(credential_null.getPasswordResetHash());
	}

	@Test
	public void setPassword_trim_value() {
		assertEquals(credential_trim.getPassword(), PASSWORD);
	}

	@Test
	public void setPasswordResetHash_trim_value() {
		assertEquals(credential_trim.getPasswordResetHash(), HASH);
	}
}
