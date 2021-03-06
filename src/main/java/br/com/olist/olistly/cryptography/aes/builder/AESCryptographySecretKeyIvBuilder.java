package br.com.olist.olistly.cryptography.aes.builder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import br.com.olist.olistly.cryptography.builder.SecretKeyBuilder;
import br.com.olist.olistly.cryptography.enumerator.CryptographyAlgorithm;
import br.com.olist.olistly.cryptography.enumerator.SecretKeyFactoryAlgorithm;
import br.com.olist.olistly.cryptography.exception.GenerateSecretException;
import br.com.olist.olistly.cryptography.model.CryptographySecretKeyIv;
import br.com.olist.olistly.cryptography.utilitary.PropertiesUtils;

public class AESCryptographySecretKeyIvBuilder {

	private String propertiesName;

	private AESCryptographySecretKeyIvBuilder(String propertiesName) {
		this.propertiesName = propertiesName;
	}

	public static AESCryptographySecretKeyIvBuilder read(String properties) {
		return new AESCryptographySecretKeyIvBuilder(properties);
	}

	public CryptographySecretKeyIv build() throws FileNotFoundException, IOException, GenerateSecretException {
		Properties properties = (new PropertiesUtils()).read(propertiesName);

		SecretKey key = genereteSecretKey(properties.getProperty("crypto.secret", ""), properties.getProperty("crypto.salt", ""));
		IvParameterSpec iv = generateIV(properties.getProperty("crypto.iv", ""));

		return new CryptographySecretKeyIv(key, iv);
	}

	private SecretKey genereteSecretKey(String secret, String salt) throws GenerateSecretException {
		return SecretKeyBuilder
					.usingAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WITHHMACSHA256)
					.usingSalt(salt)
					.withIterationCount(65536)
					.withKeyLength(128)
					.toAlgorithm(CryptographyAlgorithm.AES)
			   .build(secret);
	}

	private IvParameterSpec generateIV(String iv) throws GenerateSecretException {
		try {
			return new IvParameterSpec(iv.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException exception) {
			throw new GenerateSecretException(exception.getMessage(), exception);
		}
	}
}
