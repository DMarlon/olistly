package br.com.olist.olistly.model;

import java.util.List;

import br.com.olist.olistly.enumerator.Status;

public class QueryParam {

	private static final List<String> directions = List.of("ASC", "DESC");

	private int page;
	private int limit;
	private String order;
	private String direction;
	private Status status;
	private String term;

	public QueryParam() {
		this.page = 1;
		this.limit = 20;
		this.order = "";
		this.direction = "ASC";
		this.status = null;
		this.term = "";
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page > 0 ? page : 1;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {

		this.limit = limit > 0 ? limit : 1;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order == null ? null : order.trim();
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		if (direction == null)
			this.direction = direction;
		else {
			String formattedDirection = direction.trim().toUpperCase();
			this.direction = directions.contains(formattedDirection) ? formattedDirection : directions.get(0);
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term == null ? null : term.trim();
	}

}
