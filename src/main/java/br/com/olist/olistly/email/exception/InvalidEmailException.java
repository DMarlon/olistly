package br.com.olist.olistly.email.exception;

public class InvalidEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailException(String message) {
		super(message);
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
