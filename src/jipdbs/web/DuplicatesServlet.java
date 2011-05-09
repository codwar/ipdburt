package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.data.Server;
import jipdbs.data.ServerDAO;
import jipdbs.data.ServerDAOImpl;
import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class DuplicatesServlet extends HttpServlet {

	private static final long serialVersionUID = -8523957912890704182L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServerDAO serverDAO = new ServerDAOImpl();

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		String s = req.getParameter("s");

		Server server;
		try {
			server = serverDAO.get(KeyFactory.stringToKey(s));
		} catch (EntityNotFoundException e) {
			resp.getWriter().write("Server not found");
			return;
		}

		Query q = new Query("Player").setKeysOnly();
		q.setAncestor(server.getKey());
		PreparedQuery pq = service.prepare(q);

		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : pq.asIterable()) {
			Query q2 = new Query("Alias").setKeysOnly();
			q2.setAncestor(entity.getKey());
			q2.addSort("count", SortDirection.DESCENDING);
			PreparedQuery pq2 = service.prepare(q2);
			try {
				pq2.asSingleEntity();
			} catch (TooManyResultsException e) {
				for (Entity alias : pq2.asIterable(FetchOptions.Builder
						.withOffset(1))) {
					keys.add(alias.getKey());
				}
			}
		}
		service.delete(keys);
		LocalCache.getInstance().clear();
		resp.getWriter()
				.write("Done. Removed " + Integer.toString(keys.size()));
	}
}
