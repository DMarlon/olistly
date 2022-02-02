package br.com.olist.olistly.email.enumerator;

public enum ServiceType {
	LOCAL("Local"),
	SERVICE("Service");

	private String description;

	private ServiceType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static ServiceType getServiceType(String description) {
        for(ServiceType type : values()) {
            if(type.getDescription().toLowerCase().equals(description.trim().toLowerCase()))
            	return type;
        }

        throw new IllegalArgumentException(description+" is not a valid e-mail service!");
    }
}
