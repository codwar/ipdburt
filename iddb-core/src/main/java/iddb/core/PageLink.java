package iddb.core;

public class PageLink {

	final int pageNumber;
	final int pageSize;
	final int totalPages;

	public PageLink(int pageNumber, int pageSize, int totalPages) {
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

}
