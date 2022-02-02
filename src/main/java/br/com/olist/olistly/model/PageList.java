package br.com.olist.olistly.model;

import java.util.List;

public class PageList<T> {
	private List<T> content;
	private int currentPage;
	private int totalPages;
	private long totalRecords;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public PageList<T> withContent(List<T> content) {
		setContent(content);
		return this;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int actualPage) {
		this.currentPage = actualPage;
	}

	public PageList<T> inPage(int page) {
		setCurrentPage(page);
		return this;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public PageList<T> withTotalPages(int totalPages) {
		setTotalPages(totalPages);
		return this;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public PageList<T> withTotalRecords(long totalRecords) {
		setTotalRecords(totalRecords);
		return this;
	}
}
