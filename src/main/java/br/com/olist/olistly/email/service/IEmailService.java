package br.com.olist.olistly.email.service;

import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.email.model.Email;

public interface IEmailService {
	public void send(Email email) throws SendEmailException;
}
