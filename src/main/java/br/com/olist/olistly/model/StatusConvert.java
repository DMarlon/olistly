package br.com.olist.olistly.model;

import javax.persistence.AttributeConverter;

import br.com.olist.olistly.enumerator.Status;

public class StatusConvert implements AttributeConverter<Status, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Status status) {
		return status.getCode();
	}

	@Override
	public Status convertToEntityAttribute(Integer statuscode) {
		return Status.getStatus(statuscode);
	}

}
