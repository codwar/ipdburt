package jipdbs.web.batch;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.api.v2.Update;
import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class CleanOnlinePlayerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(CleanOnlinePlayerServlet.class.getName());
	
	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		String key = req.getParameter("key");
		String force = req.getParameter("force");
		if (key != null) {
			try {
				Server server;
				server = app.getServer(KeyFactory.stringToKey(key));
				if (server.getOnlinePlayers()>0 || "true".equals(force)) {
					Update api = new Update();
					api.cleanServer(server, false);
				}
			} catch (EntityNotFoundException e) {
				log.severe(e.getMessage());
			}
		} else {
			DatastoreService service = DatastoreServiceFactory.getDatastoreService();
			
			Query q = new Query("Server").setKeysOnly();
			q.addFilter("updated", FilterOperator.LESS_THAN_OR_EQUAL, new Date(new Date().getTime()-7200000)); // 2 horas
			PreparedQuery pq = service.prepare(q);
			
			Queue queue = QueueFactory.getDefaultQueue();
			for (Entity server : pq.asIterable()) {
				queue.add(TaskOptions.Builder.withUrl("/admin/cleanserver").param("key", KeyFactory.keyToString(server.getKey())));
			}
			resp.getWriter().write("Total: " + pq.countEntities(FetchOptions.Builder.withLimit(1000)));
		}
	}
}
