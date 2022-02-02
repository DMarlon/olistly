package br.com.olist.olistly.security.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;

public class SystemSessionDetails implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 5756766060515534795L;
	private Session session;
	private Credential credential;

	public SystemSessionDetails(Session session, Credential credential) {
		this.session = session;
		this.credential = credential;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return credential == null ? "" : credential.getPassword();
	}

	@Override
	public String getUsername() {
		return getSystemUser().getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return Status.ENABLE.equals(getSystemUser().getStatus());
	}

	@Override
	public void eraseCredentials() {
		credential.setPassword("");
	}

	public Session getSession() {
		return session;
	}

	public User getSystemUser() {
		return session.getUser();
	}

}
