package jipdbs.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.List;

import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ServerDAO {

	public void cache(Server server) {
		LocalCache.getInstance().put("server-" + server.getUid(), server);
	}
	
	public void save(DatastoreService service, Server server) {
		Entity entity = server.toEntity();
		service.put(entity);
		server.setKey(entity.getKey());
		cache(server);
	}

	public List<Server> findAll(DatastoreService service, int offset,
			int limit, int[] count) {

		Query q = new Query("Server");
		q.addSort("name", SortDirection.ASCENDING);
		
		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Server> list = new ArrayList<Server>();

		for (Entity e : pq.asIterable(withOffset(offset).limit(limit)))
			list.add(new Server(e));

		return list;
	}

	public Server findByUid(DatastoreService service, String uid) {

		// retrieve from cache
		Server s = (Server) LocalCache.getInstance().get("server-" + uid);
		if (s != null)
			return s;

		Query q = new Query("Server");
		q.addFilter("uid", FilterOperator.EQUAL, uid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return new Server(entity);

		return null;
	}

	public Server get(DatastoreService service, Key server)
			throws EntityNotFoundException {
		return new Server(service.get(server));
	}

}
