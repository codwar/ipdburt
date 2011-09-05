package jipdbs.core.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jipdbs.core.model.Server;
import jipdbs.core.model.dao.ServerDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ServerDAOImpl implements ServerDAO {

	private Server fromEntity(Entity entity) {
		Server server = new Server();
		server.setKey(entity.getKey());
		server.setPluginVersion((String) entity.getProperty("pluginversion"));
		server.setCreated((Date) entity.getProperty("created"));
		server.setUpdated((Date) entity.getProperty("updated"));
		server.setAdmin((Email) entity.getProperty("admin"));
		server.setName((String) entity.getProperty("name"));
		server.setUid((String) entity.getProperty("uid"));
		server.setOnlinePlayers(((Long) entity.getProperty("players")).intValue());
		server.setAddress((String) entity.getProperty("ip"));
		server.setMaxLevel((Long) entity.getProperty("maxlevel"));
		
		if (server.getMaxLevel()==null) {
			server.setMaxLevel(2L);
		}
		Boolean b = (Boolean) entity.getProperty("dirty");
		server.setDirty(b != null ? b : true);
		Long permission = (Long) entity.getProperty("permission");
		server.setPermission(permission != null ? permission.intValue() : 0);
		return server;
	}

	private Entity toEntity(Server server) {
		Entity entity = server.getKey() == null ? new Entity("Server")
				: new Entity(server.getKey());
		entity.setProperty("updated", server.getUpdated());
		entity.setProperty("uid", server.getUid());
		entity.setProperty("ip", server.getAddress());
		entity.setProperty("dirty", server.getDirty());
		entity.setProperty("name", server.getName());
		entity.setUnindexedProperty("created", server.getCreated());
		entity.setUnindexedProperty("pluginversion", server.getPluginVersion());
		entity.setUnindexedProperty("maxlevel", server.getMaxLevel());		
		entity.setUnindexedProperty("players", server.getOnlinePlayers());
		entity.setUnindexedProperty("admin", server.getAdmin());
		entity.setUnindexedProperty("permission", server.getPermission());
		return entity;
	}
	
	@Override
	public void save(Server server) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Entity entity = toEntity(server);
		service.put(entity);
		server.setKey(entity.getKey());
	}

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("Server");
		q.addSort("name", SortDirection.ASCENDING);

		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Server> list = new ArrayList<Server>();

		for (Entity e : pq.asList(withOffset(offset).limit(limit)))
			list.add(fromEntity(e));

		return list;
	}

	@Override
	public Server findByUid(String uid) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("Server");
		q.addFilter("uid", FilterOperator.EQUAL, uid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return fromEntity(entity);

		return null;
	}

	@Override
	public Server get(Key server) throws EntityNotFoundException {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		
		try {
			Entity entity = service.get(server);
			if (entity!=null) return fromEntity(entity);
		} catch (Exception e) {
		}
		
		return null;
	}
}
