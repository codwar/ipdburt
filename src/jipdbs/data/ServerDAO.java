package jipdbs.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ServerDAO {

	public void insert(DatastoreService service, Server server) {

		Entity entity = new Entity("Server");
		entity.setProperty("name", server.getName());
		entity.setProperty("admin", server.getAdmin());
		entity.setProperty("created", server.getCreated());
		entity.setProperty("uid", server.getUid());
		entity.setProperty("updated", server.getUpdated());

		service.put(entity);

		server.setKey(entity.getKey());
	}

	public void update(DatastoreService service, Server server)
			throws EntityNotFoundException {

		Entity entity = service.get(server.getKey());

		entity.setProperty("name", server.getName());
		entity.setProperty("admin", server.getAdmin());
		entity.setProperty("created", server.getCreated());
		entity.setProperty("uid", server.getUid());
		entity.setProperty("updated", server.getUpdated());

		service.put(entity);

		server.setKey(entity.getKey());
	}

	private Server map(Entity entity) {

		Server server = new Server();

		server.setKey(entity.getKey());
		server.setCreated((Date) entity.getProperty("created"));
		server.setAdmin((Email) entity.getProperty("admin"));
		server.setName((String) entity.getProperty("name"));
		server.setUid((String) entity.getProperty("uid"));
		server.setUpdated((Date) entity.getProperty("created"));

		return server;
	}

	public List<Server> getAll(DatastoreService service) {

		Query q = new Query("Server");
		PreparedQuery pq = service.prepare(q);

		List<Server> list = new ArrayList<Server>();

		for (Entity e : pq.asIterable())
			list.add(map(e));

		return list;
	}
}
