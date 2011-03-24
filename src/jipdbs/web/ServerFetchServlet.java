package jipdbs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;
import jipdbs.data.Server;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class ServerFetchServlet extends HttpServlet {

	private static final long serialVersionUID = 9193574096825280151L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Server server = null;
		try {
			server = app.getServer(req.getParameter("k"));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.setAttribute("server", server);
	}
	
}
