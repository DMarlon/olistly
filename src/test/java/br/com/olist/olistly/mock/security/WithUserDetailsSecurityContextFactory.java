package br.com.olist.olistly.mock.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import br.com.olist.olistly.mock.modal.system.UserMock;
import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.security.model.SystemSessionDetails;

public class WithUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		Session session = new Session();
		session.setUser(UserMock.mock());
		SystemSessionDetails principal = new SystemSessionDetails(session, new Credential());
		Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
		context.setAuthentication(auth);
		return context;
	}
}
