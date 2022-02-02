package br.com.olist.olistly.model.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionTest {
	private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlBIQml0IiwiaWF0IjoxNTE2MjM5MDIyfQ.WkAIuKoTtgJiEa3mz0tzvpXa7Dlq9wS3qtl_50AxwMU";
	private static final String DEVICENAME = "noname";
	private static final String REMOTEADDRESS = "2001:0DB8:AD1F:25E2:CADE:CAFE:F0CA:84C1";

	Session session_null;
	Session session_trim;

	@BeforeEach
	public void init() {
		session_null = new Session();
		session_null.setToken(null);
		session_null.setDeviceName(null);
		session_null.setRemoteAddress(null);

		session_trim = new Session();
		session_trim.setToken(" "+TOKEN+" ");
		session_trim.setDeviceName(" "+DEVICENAME+" ");
		session_trim.setRemoteAddress(" "+REMOTEADDRESS+" ");
	}

	@Test
	public void setToken_null_value() {
		assertNull(session_null.getToken());
	}

	@Test
	public void setDeviceName_null_value() {
		assertNull(session_null.getDeviceName());
	}

	@Test
	public void setRemoteAddress_null_value() {
		assertNull(session_null.getRemoteAddress());
	}

	@Test
	public void setToken_trim_value() {
		assertEquals(session_trim.getToken(), TOKEN);
	}

	@Test
	public void setDeviceName_trim_value() {
		assertEquals(session_trim.getDeviceName(), DEVICENAME);
	}

	@Test
	public void setRemoteAddress_trim_value() {
		assertEquals(session_trim.getRemoteAddress(), REMOTEADDRESS);
	}
}
