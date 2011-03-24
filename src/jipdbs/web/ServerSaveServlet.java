package jipdbs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datanucleus.util.StringUtils;

import jipdbs.JIPDBS;
import jipdbs.util.GuidGenerator;

public class ServerSaveServlet extends HttpServlet {

	private static final long serialVersionUID = 9193574096825280151L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (StringUtils.isEmpty(req.getParameter("k"))) {
			String uid = GuidGenerator.generate(req.getParameter("name"));
			app.addServer(req.getParameter("name"), req.getParameter("admin"), uid, req.getParameter("ip"));
		} else {
			app.saveServer(req.getParameter("k"), req.getParameter("name"), req.getParameter("admin"), req.getParameter("ip"));
		}
	}
}
