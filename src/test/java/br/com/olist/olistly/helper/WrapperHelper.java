package br.com.olist.olistly.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WrapperHelper {

	private ObjectMapper objectMapper;

	public WrapperHelper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String wrapper(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	public <T> T unwrapper(String json, Class<T> unwrapperClass) {
		try {
			return objectMapper.readValue(json, unwrapperClass);
		} catch (JsonProcessingException exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}
}
