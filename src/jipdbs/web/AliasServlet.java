package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.AliasResult;
import jipdbs.JIPDBS;

public class AliasServlet extends HttpServlet {

	private static final long serialVersionUID = 8123604074174537109L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = req.getParameter("id");

		List<AliasResult> list = new ArrayList<AliasResult>();

		list = app.alias(id, 0, 30);

		req.setAttribute("list", list);
	}
}
