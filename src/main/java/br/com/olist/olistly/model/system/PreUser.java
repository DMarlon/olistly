package br.com.olist.olistly.model.system;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "syspreusers")
public class PreUser {

	@Id
	@Column(name = "pus_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
    private Long id;

	@Column(name = "pus_login", nullable = false, unique = true)
	private String login;

	@Column(name = "pus_name", nullable = false)
	private String name;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "pus_password", nullable = false)
	private String password;

	@JsonProperty(access = Access.READ_ONLY)
	@Column(name = "pus_dateexpiration", nullable = false)
	private LocalDateTime dateExpiration;

	@JsonIgnore
	@Column(name = "pus_activationhash", nullable = false)
	private String activationHash;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(LocalDateTime dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public String getActivationHash() {
		return activationHash;
	}

	public void setActivationHash(String activationHash) {
		this.activationHash = activationHash;
	}
}
