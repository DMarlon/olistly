package br.com.olist.olistly.exception;

public class AssociatedValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssociatedValueException(String message) {
		super(message);
	}

	public AssociatedValueException(String message, Throwable cause) {
		super(message, cause);
	}
}
