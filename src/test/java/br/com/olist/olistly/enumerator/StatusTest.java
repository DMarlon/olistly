package br.com.olist.olistly.enumerator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class StatusTest {

	@Test
	public void valueOf_worng_value() {
		assertThrows(IllegalArgumentException.class, () -> {Status.getStatus(5);});
	}
}
