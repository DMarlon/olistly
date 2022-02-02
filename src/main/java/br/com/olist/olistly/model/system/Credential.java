package br.com.olist.olistly.model.system;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "sysusercredentials")
public class Credential {

	@Id
	@Column(name = "cre_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cre_user_id", nullable = false, unique = true)
	private User user;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "cre_password", nullable = false)
	private String password;

	@Column(name = "cre_passwordresethash")
	private String passwordResetHash;

	@Column(name = "cre_passwordresetexpiration")
	private LocalDateTime passwordResetExpiration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getPasswordResetHash() {
		return passwordResetHash;
	}

	public void setPasswordResetHash(String passwordResetHash) {
		this.passwordResetHash = passwordResetHash == null ? null : passwordResetHash.trim();
	}

	public LocalDateTime getPasswordResetExpiration() {
		return passwordResetExpiration;
	}

	public void setPasswordResetExpiration(LocalDateTime passwordResetExpiration) {
		this.passwordResetExpiration = passwordResetExpiration;
	}
}
