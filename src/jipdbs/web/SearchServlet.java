package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;
import jipdbs.SearchResult;

public class SearchServlet extends HttpServlet {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private static final long serialVersionUID = -729953187311026007L;

	private JIPDBS app;

	public static class PageLink {

		final String text;
		final int pageNumber;
		final int pageSize;
		final boolean enabled;

		public PageLink(String text, int pageNumber, int pageSize,
				boolean enabled) {
			this.pageNumber = pageNumber;
			this.enabled = enabled;
			this.pageSize = pageSize;
			this.text = text;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public String getText() {
			return text;
		}

		public int getPageSize() {
			return pageSize;
		}
	}

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int page = 0;
		int pageSize = DEFAULT_PAGE_SIZE;

		try {
			page = Integer.parseInt(req.getParameter("p"));
		} catch (NumberFormatException e) {
			// Ignore.
		}

		try {
			pageSize = Integer.parseInt(req.getParameter("ps"));
		} catch (NumberFormatException e) {
			// Ignore.
		}

		int offset = page * pageSize;
		int limit = pageSize;

		String query = req.getParameter("q");
		String type = req.getParameter("t");

		List<SearchResult> list = new ArrayList<SearchResult>();

		int total = 0;
		if (query == null || "".equals(query)) {
			list = app.rootQuery(offset, limit);
			total = app.rootQueryCount();
		} else {
			list = app.search(query, type, offset, limit);
			total = app.searchCount(query, type);
		}

		List<PageLink> pageLinks = new ArrayList<PageLink>();

		int totalPages = (int) Math.ceil((double) total / pageSize);

		for (int i = 0; i < totalPages; i++)
			pageLinks.add(new PageLink(Integer.toString(i + 1), i, pageSize,
					i != page));

		req.setAttribute("list", list);
		req.setAttribute("query", query);
		req.setAttribute("type", type);
		req.setAttribute("pageLinks", pageLinks);
	}
}
