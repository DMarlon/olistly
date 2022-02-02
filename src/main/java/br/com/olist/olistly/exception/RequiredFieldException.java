package br.com.olist.olistly.exception;

public class RequiredFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequiredFieldException(String message) {
		super(message);
	}

	public RequiredFieldException(String message, Throwable cause) {
		super(message, cause);
	}
}
