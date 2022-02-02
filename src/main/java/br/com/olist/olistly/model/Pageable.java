package br.com.olist.olistly.model;

public class Pageable {
	private int limit;
	private int currentPage;
	private int totalOfRecords;

	public Pageable(int currentPage, int limit, int totalOfRecords) {
		this.limit = limit < 1 ? 1 : limit;
		this.currentPage = currentPage < 1 ? 1 : currentPage;
		this.totalOfRecords = totalOfRecords < 0 ? 0 : totalOfRecords;
	}

	public int getTotalPage() {
		return calculateTotalPages();
	}

	public int getStartPosition() {
		return calculateStartPosition();
	}

	public int getLimit() {
		return limit;
	}

	public int getTotalOfRecords() {
		return totalOfRecords;
	}

	private int calculateStartPosition() {
		int totalOfPages = getTotalPage();
		if (totalOfPages == 0)
			return totalOfPages;

		return ((currentPage > totalOfPages ? (totalOfPages - 1) : currentPage - 1) * limit);
	}

	private int calculateTotalPages() {
		if (totalOfRecords == 0)
			return totalOfRecords;

		return totalOfRecords < limit ? 1 : (totalOfRecords / limit) + (totalOfRecords % limit == 0 ? 0 : 1);
	}
}
