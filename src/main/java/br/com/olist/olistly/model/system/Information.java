package br.com.olist.olistly.model.system;

public class Information {
	private String name;
	private String homepage;
	private String frontendURL;
	private String backendURL;
	private String contact;
	private String noReply;

	public Information(String name, String homepage, String frontendURL, String backendURL, String contact, String noReply) {
		this.name = name == null ? null : name.trim();
		this.homepage = homepage == null ? null : homepage.trim();
		this.frontendURL = frontendURL == null ? null : frontendURL.trim();
		this.backendURL = backendURL == null ? null : backendURL.trim();
		this.contact = contact == null ? null : contact.trim();
		this.noReply = noReply == null ? null : noReply.trim();
	}

	public String getName() {
		return name;
	}

	public String getHomepage() {
		return homepage;
	}

	public String getFrontendURL() {
		return frontendURL;
	}

	public String getBackendURL() {
		return backendURL;
	}

	public String getContact() {
		return contact;
	}

	public String getNoReply() {
		return noReply;
	}
}
