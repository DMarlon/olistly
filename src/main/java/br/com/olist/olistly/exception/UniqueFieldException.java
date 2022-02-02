package br.com.olist.olistly.exception;

public class UniqueFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UniqueFieldException(String message) {
		super(message);
	}

	public UniqueFieldException(String message, Throwable cause) {
		super(message, cause);
	}
}
