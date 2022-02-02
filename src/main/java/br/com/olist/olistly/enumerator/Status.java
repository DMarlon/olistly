package br.com.olist.olistly.enumerator;

public enum Status {
	ENABLE(1, "Enable"),
	DISABLE(2, "Disable");

	private int code;
	private String description;

	private Status(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public static Status getStatus(int code) {
        for(Status status : values()) {
            if(Integer.valueOf(status.getCode()).equals(code))
            	return status;
        }

        throw new IllegalArgumentException(code+" is not a valid status");
    }

	@Override
	public String toString() {
		return this.getDescription();
	}
}
