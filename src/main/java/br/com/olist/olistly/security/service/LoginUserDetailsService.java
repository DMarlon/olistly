package br.com.olist.olistly.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.security.model.SystemSessionDetails;
import br.com.olist.olistly.service.system.CredentialService;
import br.com.olist.olistly.service.system.UserService;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	private UserService userService;
	private CredentialService credentialService;

    public LoginUserDetailsService(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByLogin(username);

        if (user == null || user.getId() <= Long.valueOf(0)) {
            throw new UsernameNotFoundException(username);
        }

        Session session = new Session();
        session.setUser(user);

        Optional<Credential> credential = credentialService.findByUser(user);

        return new SystemSessionDetails(session, credential.get());
    }
}
