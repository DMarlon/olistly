package br.com.olist.olistly.model;

public class JWTInfo {

	private String secret;

	public JWTInfo(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}
}
