package br.com.olist.olistly.model.system;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.model.StatusConvert;

@Entity
@Table(name = "sysusers")
public class User {

	@Id
	@Column(name = "use_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "use_login", nullable = false, unique = true)
	private String login;

	@Column(name = "use_name", nullable = false)
	private String name;

	@Column(name = "use_status", nullable = false, columnDefinition = "status default 1")
	@Convert(converter = StatusConvert.class)
	private Status status;

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
		this.login = login == null ? null : login.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
