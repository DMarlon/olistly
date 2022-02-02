package br.com.olist.olistly.model.url;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UrlAddressTest {
	private static final String ORIGINALURL = "http://teste.com";
	private static final String SHORTENEDHASH = "superhash";

	UrlAddress urlAddress_null;
	UrlAddress urlAddress_trim;

	@BeforeEach
	public void init() {
		urlAddress_null = new UrlAddress();
		urlAddress_null.setOriginalUrl(null);
		urlAddress_null.setShortenedHash(null);

		urlAddress_trim = new UrlAddress();
		urlAddress_trim.setOriginalUrl(" "+ORIGINALURL+" ");
		urlAddress_trim.setShortenedHash(" "+SHORTENEDHASH+" ");
	}

	@Test
	public void setOriginal_null_value() {
		assertNull(urlAddress_null.getOriginalUrl());
	}

	@Test
	public void setShortenedHash_null_value() {
		assertNull(urlAddress_null.getShortenedHash());
	}

	@Test
	public void setOriginalUrl_trim_value() {
		assertEquals(urlAddress_trim.getOriginalUrl(), ORIGINALURL);
	}

	@Test
	public void setShortenedHash_trim_value() {
		assertEquals(urlAddress_trim.getShortenedHash(), SHORTENEDHASH);
	}
}
