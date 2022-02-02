package br.com.olist.olistly.utilitary;

import java.util.Objects;

public class HTTPUtils {
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String HTTP = "http";

	public static String setHttpProtocol(String url) {
		if (Objects.isNull(url) || url.trim().isEmpty())
			return null;

		return HTTP.concat("://").concat(url);
	}
}
