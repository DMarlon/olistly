package br.com.olist.olistly.dto.system;

public class UserChangePasswordDTO {
	private String currentPassword;
	private String newPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword == null ? null : currentPassword.trim();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword == null ? null : newPassword.trim();
	}
}
