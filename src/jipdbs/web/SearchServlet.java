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

	private static final long serialVersionUID = -729953187311026007L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String alias = req.getParameter("alias");

		List<SearchResult> list = new ArrayList<SearchResult>();

		if (alias == null || "".equals(alias))
			list = app.rootQuery();
		else
			list = app.search(alias);

		req.setAttribute("list", list);
	}
}
