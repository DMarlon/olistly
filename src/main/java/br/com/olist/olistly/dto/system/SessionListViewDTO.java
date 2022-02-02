package br.com.olist.olistly.dto.system;

import java.time.LocalDateTime;

public interface SessionListViewDTO {
	public Long getId();
	public String getDeviceName();
	public String getRemoteAddress();
	public LocalDateTime getLastAccess();
	public LocalDateTime getExpiration();
	public Boolean getCurrent();
}
