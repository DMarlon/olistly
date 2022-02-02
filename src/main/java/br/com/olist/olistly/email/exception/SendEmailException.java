package br.com.olist.olistly.email.exception;

public class SendEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SendEmailException(String message) {
		super(message);
	}

	public SendEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
