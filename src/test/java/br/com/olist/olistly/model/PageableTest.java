package br.com.olist.olistly.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PageableTest {

	@Test
	public void create_with_negative_values() {
		Pageable pageable = new Pageable(-15, -30, -60);
		assertEquals(0, pageable.getTotalPage());
		assertEquals(0, pageable.getStartPosition());
		assertEquals(1, pageable.getLimit());
		assertEquals(0, pageable.getTotalOfRecords());
	}

	@Test
	public void calculate_values() {
		Pageable pageable = new Pageable(2, 5, 10);
		assertEquals(2, pageable.getTotalPage());
		assertEquals(5, pageable.getStartPosition());
		assertEquals(5, pageable.getLimit());
		assertEquals(10, pageable.getTotalOfRecords());

		pageable = new Pageable(3, 5, 21);
		assertEquals(5, pageable.getTotalPage());
		assertEquals(10, pageable.getStartPosition());
		assertEquals(5, pageable.getLimit());
		assertEquals(21, pageable.getTotalOfRecords());
	}
}
