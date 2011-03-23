package jipdbs.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class ServerDAO {

	private Entity map(Server server) {

		Entity entity = server.getKey() == null ? new Entity("Server")
				: new Entity(server.getKey());

		entity.setProperty("name", server.getName());
		entity.setProperty("created", server.getCreated());
		entity.setProperty("updated", server.getUpdated());
		entity.setProperty("admin", server.getAdmin());
		entity.setProperty("uid", server.getUid());
		entity.setProperty("players", server.getOnlinePlayers());
		entity.setProperty("ip", server.getAddress());

		return entity;
	}

	private Server map(Entity entity) {

		Server server = new Server();

		server.setKey(entity.getKey());
		server.setCreated((Date) entity.getProperty("created"));
		server.setUpdated((Date) entity.getProperty("updated"));
		server.setAdmin((Email) entity.getProperty("admin"));
		server.setName((String) entity.getProperty("name"));
		server.setUid((String) entity.getProperty("uid"));
		server.setOnlinePlayers(((Long) entity.getProperty("players"))
				.intValue());
		server.setAddress((String) entity.getProperty("ip"));

		return server;
	}

	public void save(DatastoreService service, Server server) {
		Entity entity = map(server);
		service.put(entity);
		server.setKey(entity.getKey());
		// save to cache
		LocalCache.getInstance().put("server-" + server.getUid(), server);
	}

	public List<Server> findAll(DatastoreService service, int offset,
			int limit, int[] count) {

		Query q = new Query("Server");
		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Server> list = new ArrayList<Server>();

		for (Entity e : pq.asIterable(withOffset(offset).limit(limit)))
			list.add(map(e));

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
			return map(entity);

		return null;
	}

	public Server get(DatastoreService service, Key server)
			throws EntityNotFoundException {
		return map(service.get(server));
	}

}
