package br.com.olist.olistly.mock.dto.system;

import br.com.olist.olistly.dto.system.PasswordResetDTO;

public class PasswordResetDTOMock {
	private static final String LOGIN = "nothing@valid.com";
	private static final String PASSWORD = "superpassword";

	public static PasswordResetDTO mock() {
		PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
		passwordResetDTO.setLogin(LOGIN);
		passwordResetDTO.setPassword(PASSWORD);
		return passwordResetDTO;
	}

}
