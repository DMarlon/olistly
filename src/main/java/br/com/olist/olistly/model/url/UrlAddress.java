package br.com.olist.olistly.model.url;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.model.StatusConvert;
import br.com.olist.olistly.model.system.User;

@Entity
@Table(name = "urladdresses")
public class UrlAddress {

	@Id
	@Column(name = "uad_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uad_user_id", nullable = false)
	private User user;

	@Column(name = "uad_originalurl", nullable = false)
	private String originalUrl;

	@Column(name = "uad_shortenedhash", nullable = false)
	private String shortenedHash;

	@Column(name = "uad_datecreated", nullable = false)
	private LocalDateTime dateCreated;

	@Column(name = "uad_status", nullable = false, columnDefinition = "status default 1")
	@Convert(converter = StatusConvert.class)
	private Status status;

	@Formula(value ="(SELECT COUNT(u.uav_id) FROM urladdressviews u where u.uav_urladdress_id = uad_id)")
	private Long views;

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

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl == null ? null : originalUrl.trim().toLowerCase();
	}

	public String getShortenedHash() {
		return shortenedHash;
	}

	public void setShortenedHash(String shortenedHash) {
		this.shortenedHash = shortenedHash == null ? null : shortenedHash.trim();
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}
}
