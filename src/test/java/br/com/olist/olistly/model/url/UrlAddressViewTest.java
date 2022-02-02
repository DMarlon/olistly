package br.com.olist.olistly.model.url;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UrlAddressViewTest {
	private static final String DEVICENAME = "Firefox";
	private static final String REMOTEADDRESS = "superip";

	UrlAddressView urlAddressView_null;
	UrlAddressView urlAddressView_trim;

	@BeforeEach
	public void init() {
		urlAddressView_null = new UrlAddressView();
		urlAddressView_null.setDeviceName(null);
		urlAddressView_null.setRemoteAddress(null);

		urlAddressView_trim = new UrlAddressView();
		urlAddressView_trim.setDeviceName(" "+DEVICENAME+" ");
		urlAddressView_trim.setRemoteAddress(" "+REMOTEADDRESS+" ");
	}

	@Test
	public void setDeviceName_null_value() {
		assertNull(urlAddressView_null.getDeviceName());
	}

	@Test
	public void setRemoteAddress_null_value() {
		assertNull(urlAddressView_null.getRemoteAddress());
	}

	@Test
	public void setDeviceName_trim_value() {
		assertEquals(urlAddressView_trim.getDeviceName(), DEVICENAME);
	}

	@Test
	public void setRemoteAddress_trim_value() {
		assertEquals(urlAddressView_trim.getRemoteAddress(), REMOTEADDRESS);
	}
}
