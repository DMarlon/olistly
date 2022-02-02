package br.com.olist.olistly.email.exception;

public class AttachmentEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AttachmentEmailException(String message) {
		super(message);
	}

	public AttachmentEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
