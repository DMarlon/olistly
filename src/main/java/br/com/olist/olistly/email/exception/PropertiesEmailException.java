package br.com.olist.olistly.email.exception;

public class PropertiesEmailException extends RuntimeException {

	private static final long serialVersionUID = -7347344479549421189L;

	public PropertiesEmailException(String message) {
		super(message);
	}

	public PropertiesEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}

