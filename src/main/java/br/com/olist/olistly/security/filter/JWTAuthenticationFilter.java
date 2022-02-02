package br.com.olist.olistly.security.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.olist.olistly.model.JWTInfo;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.security.adapter.JWTAdapter;
import br.com.olist.olistly.security.exception.JWTTokenInvalidException;
import br.com.olist.olistly.security.model.SystemSessionDetails;
import br.com.olist.olistly.service.system.SessionService;
import br.com.olist.olistly.utilitary.HTTPUtils;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private SessionService sessionService;
	private JWTInfo jwtInfo;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SessionService sessionService, JWTInfo jwtInfo) {
		super(authenticationManager);
		this.sessionService = sessionService;
		this.jwtInfo = jwtInfo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(HTTPUtils.AUTHORIZATION);
		if (header == null || !header.startsWith(HTTPUtils.BEARER)) {
			chain.doFilter(request, response);
		} else {
			try {
				SecurityContextHolder.getContext().setAuthentication(getAuthentication(header.replace(HTTPUtils.BEARER, "")));
				chain.doFilter(request, response);
			} catch (Exception exception) {
				response.sendError(HttpStatus.FORBIDDEN.value(), exception.getMessage());
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		}
	}

	private Authentication getAuthentication(String token) throws JWTTokenInvalidException {
		Session session = sessionService.getByToken(token);
		if (session != null) {
			String username = JWTAdapter.getUserName(token, jwtInfo.getSecret());
			if (username != null) {
				return new UsernamePasswordAuthenticationToken(new SystemSessionDetails(session, null), null, Collections.emptyList());
			}
		}

		return null;
	}
}
