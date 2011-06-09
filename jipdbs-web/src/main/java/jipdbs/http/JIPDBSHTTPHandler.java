package jipdbs.http;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class JIPDBSHTTPHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 581879616460472029L;
	private static final Logger log = Logger.getLogger(JIPDBSHTTPHandler.class
			.getName());

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		this.app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if ("/fetchserver".equalsIgnoreCase(req.getPathInfo())) {
			resp.setContentType("text/plain");
			String key = req.getParameter("key");
			Server server = null;
			try {
				server = app.getServer(key);
			} catch (EntityNotFoundException e) {
				log.severe(e.getMessage());
			}
			if (server == null) {
				resp.getWriter().println(
						"{\"error\": true, \"server\": {\"key\": \"" + key
								+ "\"}}");
			} else {
				if (server.getDirty()) {
					app.refreshServerInfo(server);
				}
				resp.getWriter().println(
						"{\"error\": false, \"server\": {\"key\": \""
								+ server.getKeyString() + "\", \"count\": \""
								+ server.getOnlinePlayers() + "\",\"name\": \""
								+ server.getName() + "\"}}");
			}
		}
	}
}
