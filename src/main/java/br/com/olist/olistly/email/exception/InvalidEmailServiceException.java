package br.com.olist.olistly.email.exception;

public class InvalidEmailServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailServiceException(String message) {
		super(message);
	}

	public InvalidEmailServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
