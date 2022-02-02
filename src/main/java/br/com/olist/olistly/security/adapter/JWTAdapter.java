package br.com.olist.olistly.security.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import br.com.olist.olistly.security.exception.JWTTokenInvalidException;

@Component
public class JWTAdapter {

	public static String createToken(String username, LocalDateTime expiration, String secret) {
		return JWT.create()
				.withSubject(username)
				.withClaim("olistly", "Test Olist")
                .withExpiresAt(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secret));
	}

	public static LocalDateTime addExpirationTime(LocalDateTime date) {
		LocalDateTime dateexpiration = LocalDateTime.of(date.toLocalDate(), date.toLocalTime());
		return dateexpiration.plusDays(10);
	}

	public static String getUserName(String token, String secret) throws JWTTokenInvalidException {
		try {
			return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject();
		} catch (TokenExpiredException exception) {
			throw new JWTTokenInvalidException("Token expired!");
		} catch (JWTVerificationException exception) {
			throw new JWTTokenInvalidException("Token invalid!");
		}
	}
}
