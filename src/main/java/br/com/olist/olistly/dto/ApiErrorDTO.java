package br.com.olist.olistly.dto;

import java.time.LocalDateTime;

public class ApiErrorDTO {

	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;

	public static ApiErrorDTO valueOf(int status, String error, String message, String path) {
		ApiErrorDTO apiErrorDTO = new ApiErrorDTO();
		apiErrorDTO.setTimestamp(LocalDateTime.now());
		apiErrorDTO.setStatus(status);
		apiErrorDTO.setError(error);
		apiErrorDTO.setMessage(message);
		apiErrorDTO.setPath(path);

		return apiErrorDTO;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
