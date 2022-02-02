package br.com.olist.olistly.security.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.olist.olistly.dto.system.LoginDTO;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.security.model.SystemSessionDetails;
import br.com.olist.olistly.service.system.SessionService;
import br.com.olist.olistly.utilitary.HTTPUtils;

public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private SessionService sessionService;
	private AuthenticationManager authenticationManager;

    public JWTUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, SessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
            LoginDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
            return authenticationManager.authenticate(createAuthenticate(login.getLogin(), login.getPassword()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	private Authentication createAuthenticate(String login, String password) {
		return new UsernamePasswordAuthenticationToken(login, password, Collections.emptyList());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		SystemSessionDetails user = (SystemSessionDetails) auth.getPrincipal();

		Session session = sessionService.register(user.getSystemUser(), getRemoteIP(request), request.getHeader("User-Agent"));
		sessionService.removeExpiredSessions(session.getUser());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.addHeader(HTTPUtils.AUTHORIZATION, HTTPUtils.BEARER + session.getToken());
		response.getWriter().append(createUserInfo(session));

	}

	private String getRemoteIP(HttpServletRequest request) {
		String remoteIP = request.getHeader("X-FORWARDED-FOR");
		if (remoteIP == null)
			remoteIP = request.getRemoteAddr();

		return remoteIP;
	}

	private String createUserInfo(Session session) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode userinfo = mapper.readValue(mapper.writeValueAsString(session.getUser()), ObjectNode.class);

			userinfo.remove("id");
			userinfo.put("token", session.getToken());
	        return mapper.writeValueAsString(userinfo);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

}
