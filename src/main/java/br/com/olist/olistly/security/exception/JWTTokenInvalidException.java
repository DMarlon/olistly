package br.com.olist.olistly.security.exception;

public class JWTTokenInvalidException extends Exception {
	private static final long serialVersionUID = 358995941795348910L;

	public JWTTokenInvalidException(String message) {
		super(message);
	}
}
