package br.com.olist.olistly.model.url;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "urladdressviews")
public class UrlAddressView {

	@Id
	@Column(name = "uav_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uav_urladdress_id", nullable = false)
	private UrlAddress urlAddress;

	@Column(name = "uav_devicename")
	private String deviceName;

	@Column(name = "uav_remoteaddress")
	private String remoteAddress;

	@Column(name = "uav_dateaccess", nullable = false)
	private LocalDateTime dateAccess;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UrlAddress getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(UrlAddress urlAddress) {
		this.urlAddress = urlAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName == null ? null : deviceName.trim();
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress == null ? null : remoteAddress.trim();
	}

	public LocalDateTime getDateAccess() {
		return dateAccess;
	}

	public void setDateAccess(LocalDateTime dateAccess) {
		this.dateAccess = dateAccess;
	}
}
