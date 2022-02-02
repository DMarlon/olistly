package br.com.olist.olistly.cryptography.utilitary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public Properties read(String propertiesName) throws IOException, FileNotFoundException {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesName);

		if (inputStream == null)
			throw new FileNotFoundException("Configuraton file '" + propertiesName + "' not found in project");

		properties.load(inputStream);
		return properties;
	}
}
