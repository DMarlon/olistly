package br.com.olist.olistly.security.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.security.model.SystemSessionDetails;

@Service(value = "security")
public class SecurityService {

	public static Session getCurrentSession() {
		return ((SystemSessionDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSession();
	}

	public static User getLoggedUser() {
		return ((SystemSessionDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSystemUser();
	}

}