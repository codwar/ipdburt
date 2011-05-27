package jipdbs.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;

public class ServerListServlet extends HttpServlet {

	private static final long serialVersionUID = 9193574096825280151L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		int[] count = new int[1];
		List<Server> servers = app.getServers(0, 30, count);
		req.setAttribute("servers", servers);
		req.setAttribute("count", count[0]);
	}
}
