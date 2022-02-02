package br.com.olist.olistly.email.service;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.olist.olistly.email.exception.PropertiesEmailException;
import br.com.olist.olistly.email.exception.SendEmailException;
import br.com.olist.olistly.email.model.Email;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.utilitary.ValidatorUtils;

public class LocalEmailService implements IEmailService {

	private String user;
	private String password;
	private Session session;

	public LocalEmailService(PropertiesEmailService propertiesEmailService) {
		initConfiguration(propertiesEmailService);
	}

	private void initConfiguration(PropertiesEmailService propertiesEmailService) throws PropertiesEmailException {
		Properties properties = getProperties(propertiesEmailService);

		checkConfiguration(properties);
		loadConfiguration(properties);
	}

	private Properties getProperties(PropertiesEmailService propertiesEmailService) throws PropertiesEmailException {
		try {
			return propertiesEmailService.read();
		} catch (IOException exception) {
			throw new PropertiesEmailException("Error to read email configurations properties!");
		}
	}

	private void checkConfiguration(Properties properties) throws PropertiesEmailException {
		user = properties.getProperty("user");
		password = properties.getProperty("password");

		if (user == null || user.trim().isEmpty())
			throw new PropertiesEmailException("The configuration 'key' need be declared and not accept empty values!");
		if (password == null || password.trim().isEmpty())
			throw new PropertiesEmailException("The configuration 'password' need be declared and not accept empty values!");
	}

	private void loadConfiguration(Properties properties) throws PropertiesEmailException {
		try {
			properties.remove("user");
			properties.remove("password");

			session = getSession(user.trim(), password.trim(), properties);
		} catch (Exception exception) {
			throw new PropertiesEmailException("Error to generate email session!");
		}
	}

	public Session getSession(String user, String password, Properties properties) {
		return Session.getInstance(properties, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(user, password);
		    }
		});
	}

	public void send(Email email) throws SendEmailException {
		checkRequiredFields(email);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(email.getFrom());
			message.setRecipients(Message.RecipientType.TO, email.getTo().toArray(new Address[email.getTo().size()]));
			message.setSubject(email.getSubject());

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(email.getMessageHTML(), "text/html; charset=UTF-8");

	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(mimeBodyPart);

	        for (MimeBodyPart attachment : email.getAttachment())
	        	multipart.addBodyPart(attachment);

	        message.setText(email.getMessageText());
			message.setContent(multipart);

			Transport.send(message);
		} catch (MessagingException exception) {
			 throw new SendEmailException("Erro ao enviar e-mail: "+exception.getMessage());
		}
	}

	private void checkRequiredFields(Email email) {
		if (Objects.isNull(email.getFrom()) || ValidatorUtils.isNullOrEmpty(email.getFrom().toString()))
			throw new RequiredFieldException("Deve ser informado o remetente do e-mail!");
		if (Objects.isNull(email.getTo()) || email.getTo().isEmpty())
			throw new RequiredFieldException("Deve ser informado ao menos um destinatario no e-mail!");
	}
}
