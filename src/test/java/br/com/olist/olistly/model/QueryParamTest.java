package br.com.olist.olistly.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class QueryParamTest {

	@Test
	public void create_default_values() {
		QueryParam queryParam = new QueryParam();
		assertEquals(1, queryParam.getPage());
		assertEquals(20, queryParam.getLimit());
		assertEquals("", queryParam.getOrder());
		assertEquals("ASC", queryParam.getDirection());
		assertNull(queryParam.getStatus());
		assertEquals("", queryParam.getTerm());
	}

	@Test
	public void setPage_negative_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setPage(-15);

		assertEquals(1, queryParam.getPage());
	}

	@Test
	public void setLimit_negative_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setLimit(-15);

		assertEquals(1, queryParam.getLimit());
	}

	@Test
	public void setOrder_null_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setOrder(null);

		assertNull(queryParam.getOrder());
	}

	@Test
	public void setOrder_trim_value() {
		String order = "order";
		QueryParam queryParam = new QueryParam();
		queryParam.setOrder(" " + order + "  ");

		assertEquals(order, queryParam.getOrder());
	}

	@Test
	public void setDirection_invalid_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setOrder("INVERTER");

		assertEquals("ASC", queryParam.getDirection());
	}

	@Test
	public void setDirection_trim_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setDirection(" DEsC ");

		assertEquals("DESC", queryParam.getDirection());
	}

	@Test
	public void setTerm_null_value() {
		QueryParam queryParam = new QueryParam();
		queryParam.setTerm(null);

		assertNull(queryParam.getTerm());
	}

	@Test
	public void setTerm_trim_value() {
		String term = "term";
		QueryParam queryParam = new QueryParam();
		queryParam.setTerm(" " + term + "  ");

		assertEquals(term, queryParam.getTerm());
	}

}
