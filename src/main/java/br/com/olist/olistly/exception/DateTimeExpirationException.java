package br.com.olist.olistly.exception;

public class DateTimeExpirationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DateTimeExpirationException(String message) {
		super(message);
	}

	public DateTimeExpirationException(String message, Throwable cause) {
		super(message, cause);
	}
}
