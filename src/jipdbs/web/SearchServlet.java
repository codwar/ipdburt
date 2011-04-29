package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;
import jipdbs.PageLink;
import jipdbs.bean.SearchResult;
import jipdbs.util.Functions;

import org.datanucleus.util.StringUtils;

public class SearchServlet extends HttpServlet {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private static final long serialVersionUID = -729953187311026007L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int page = 1;
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

		int offset = (page - 1) * pageSize;
		int limit = pageSize;

		String query = req.getParameter("q");
		String type = req.getParameter("t");

		List<SearchResult> list = new ArrayList<SearchResult>();

		int[] total = new int[1];

		long time = System.currentTimeMillis();

		// this is to get the modified value and show it in search box
		String queryValue = query;

		if (StringUtils.isEmpty(type)) {
			list = app.rootQuery(offset, limit, total);
		} else if ("ip".equals(type)) {
			query = Functions.fixIp(query);
			queryValue = query;
			list = app.ipSearch(query, offset, limit, total);
		} else if ("s".equals(type)) {
			queryValue = "";
			list = app.byServerSearch(query, offset, limit, total);
		} else if ("ban".equals(type)) {
			queryValue = "";
			list = app.bannedQuery(offset, limit, total);
		} else if ("alias".equals(type)) {
			if (validPlayerNameChars(query)) {
				boolean[] exactMatch = new boolean[1];
				exactMatch[0] = true;
				list = app.aliasSearch(query, offset, limit, total, exactMatch);
				if (!exactMatch[0] && list.size() > 0) {
					Flash.info(
							req,
							"No se encontraron resultados precisos. "
									+ "Los resultados mostrados son variaciones del nombre.");
					Flash.warn(req, "Su búsqueda arroja demasiados resultados."
							+ " Por favor, refine su búsqueda.");
				}
			} else
				Flash.error(req, "Consulta inválida. Caracteres inválidos.");
		} else {
			Flash.warn(req, "Tipo de consulta inválido.");
		}

		time = System.currentTimeMillis() - time;

		int totalPages = (int) Math.ceil((double) total[0] / pageSize);

		req.setAttribute("list", list);
		req.setAttribute("queryValue", queryValue);
		req.setAttribute("query", query);
		req.setAttribute("type", type);
		req.setAttribute("count", total[0]);
		req.setAttribute("time", time);
		req.setAttribute("pageLink", new PageLink(page, pageSize, totalPages));
	}

	private static boolean validPlayerNameChars(String query) {
		if (query == null)
			return false;

		for (int i = 0; i < query.length(); i++)
			if (!validPlayerNameChar(query.charAt(i)))
				return false;
		return true;
	}

	private static boolean validPlayerNameChar(char c) {
		// Continuously improve this.
		return c < 256 && c != ' ';
	}
}
