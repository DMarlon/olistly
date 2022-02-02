package br.com.olist.olistly.email.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesEmailService {

	private final String propertiesName = "email.properties";

	public Properties read() throws IOException, FileNotFoundException {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesName);

		if (inputStream == null)
			throw new FileNotFoundException("Configuraton file '" + propertiesName + "' not found in project");

		properties.load(inputStream);
		return properties;
	}
}
