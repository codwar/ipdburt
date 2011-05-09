package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class DuplicatesServlet extends HttpServlet {

	private static final long serialVersionUID = -8523957912890704182L;

	private static final Logger log = Logger.getLogger(DuplicatesServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServerDAO serverDAO = new ServerDAOImpl();

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		String s = req.getParameter("s");
		String t = req.getParameter("t");
		
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
		int total = 0;
		for (Entity player : pq.asIterable()) {
			Query qAlias = new Query("Alias");
			qAlias.setAncestor(player.getKey());
			qAlias.addSort("updated", SortDirection.DESCENDING);
			PreparedQuery pqAlias = service.prepare(qAlias);
			List<String> aliasList = new ArrayList<String>();
			for (Entity alias : pqAlias.asIterable()) {
				String key = ((String) alias.getProperty("nickname")) + "-" + ((Long) alias.getProperty("ip")).toString();
				if (aliasList.contains(key)) {
					keys.add(alias.getKey());
					total += 1;
				} else {
					aliasList.add(key);
				}
			}
			if (t == null) {
				if (keys.size() > 100) {
					service.delete(keys);
					keys.clear();
					LocalCache.getInstance().clear();
				}
			}
		}
		if (t == null) {
			resp.getWriter()
					.write("Done. Removed " + Integer.toString(total));
		} else {
			resp.getWriter()
			.write(Integer.toString(total) + " are going to be removed.");
		}
	}
}
