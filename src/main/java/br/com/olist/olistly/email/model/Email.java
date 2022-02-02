package br.com.olist.olistly.email.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;

import br.com.olist.olistly.email.exception.AttachmentEmailException;
import br.com.olist.olistly.email.exception.InvalidEmailException;

public class Email {

	private Address from;
	private List<Address> to;
	private String subject;
	private String messageText;
	private String messageHTML;
	private List<MimeBodyPart> attachments;

	public Email() {
		this.from = null;
		this.subject = "";
		this.messageText = "";
		this.messageHTML = "";
		this.to = new ArrayList<Address>();
		this.attachments = new ArrayList<MimeBodyPart>();
	}

	public Address getFrom() {
		return from;
	}

	public Email from(String email) throws InvalidEmailException {
		from = createInternetAddress(email);
		return this;
	}

	public List<Address> getTo() {
		return to;
	}

	public Email to(List<String> emails) throws InvalidEmailException {
		for (String email : emails)
			addTo(email);

		return this;
	}

	public Email addTo(String email) throws InvalidEmailException {
		to.add(createInternetAddress(email));

		return this;
	}

	public String getSubject() {
		return subject;
	}

	public Email subject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getMessageText() {
		return messageText;
	}

	public Email messageText(String message) {
		messageText = message;
		return this;
	}

	public String getMessageHTML() {
		return messageHTML;
	}

	public Email messageHTML(String message) {
		messageHTML = message;
		return this;
	}

	public List<MimeBodyPart> getAttachment() {
		return attachments;
	}

	public Email addAttachment(File file) throws AttachmentEmailException {
		try {
			MimeBodyPart attachment = new MimeBodyPart();
			attachment.attachFile(file);

			attachments.add(attachment);
			return this;
		} catch (IOException | MessagingException exception) {
			throw new AttachmentEmailException("Erro ao anexar arquivo!", exception);
		}
	}

	private InternetAddress createInternetAddress(String email) throws InvalidEmailException {
		try {
			InternetAddress address = new InternetAddress(email);
			address.validate();
			return address;
		} catch (AddressException exception) {
			throw new InvalidEmailException("Email "+email+" invalido!", exception);
		}
	}
}
