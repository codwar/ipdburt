package iddb.runtime.gae.model.dao.impl;

import iddb.core.model.UserServer;
import iddb.core.model.dao.UserServerDAO;

import java.util.ArrayList;
import java.util.List;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserServerDAOImpl implements UserServerDAO {

	public UserServer(Entity entity) {
		this.key = entity.getKey();
		this.user = entity.getParent();
		this.server = (Key) entity.getProperty("server");
		this.owner = (Boolean) entity.getProperty("owner");
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("UserServer", this.user) : new Entity(this.key);
		entity.setProperty("server", this.server);
		entity.setUnindexedProperty("owner", this.owner);
		return entity;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#save(jipdbs.core.model.UserServer
	 * )
	 */
	@Override
	public void save(UserServer userServer) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Entity entity = userServer.toEntity();
		service.put(entity);
		userServer.setKey(entity.getKey());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#findByUser(com.google.appengine
	 * .api.datastore.Key)
	 */
	@Override
	public List<UserServer> findByUser(Key user) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("UserServer");
		q.setAncestor(user);

		PreparedQuery pq = service.prepare(q);

		List<UserServer> list = new ArrayList<UserServer>();

		for (Entity entity : pq.asIterable()) {
			list.add(new UserServer(entity));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#get(com.google.appengine.api
	 * .datastore.Key)
	 */
	@Override
	public UserServer get(Key userServer) throws EntityNotFoundException {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return new UserServer(service.get(userServer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#findByUserAndServer(com.google
	 * .appengine.api.datastore.Key, com.google.appengine.api.datastore.Key)
	 */
	@Override
	public UserServer findByUserAndServer(Key user, Key server) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("UserServer");
		q.setAncestor(user);
		q.addFilter("server", FilterOperator.EQUAL, server);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();
		if (entity != null) {
			return new UserServer(entity);
		}
		return null;
	}

	@Override
	public List<UserServer> findByServer(Key server) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("UserServer");
		q.addFilter("server", FilterOperator.EQUAL, server);

		PreparedQuery pq = service.prepare(q);

		List<UserServer> list = new ArrayList<UserServer>();

		for (Entity entity : pq.asIterable()) {
			list.add(new UserServer(entity));
		}

		return list;
	}

}
