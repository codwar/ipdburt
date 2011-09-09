package iddb.runtime.gae.model.dao.impl;

import iddb.core.model.UserServer;
import iddb.core.model.dao.UserServerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserServerDAOImpl implements UserServerDAO {

	public static final String ENTITY = "UserServer";
	
	private UserServer fromEntity(Entity entity) {
		UserServer user = new UserServer();
		user.setKey(entity.getKey().getId());
		user.setUser(entity.getParent().getId());
		user.setServer(((Key) entity.getProperty("server")).getId());
		user.setOwner((Boolean) entity.getProperty("owner"));
		return user;
	}

	private Entity toEntity(UserServer user) {
		Entity entity = user.getKey() == null ? new Entity(ENTITY, KeyFactory.createKey(UserDAOImpl.ENTITY, user.getUser())) : new Entity(KeyFactory.createKey(ENTITY, user.getKey()));
		entity.setProperty("server", KeyFactory.createKey(ServerDAOImpl.ENTITY, user.getServer()));
		entity.setUnindexedProperty("owner", user.getOwner());
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

		Entity entity = toEntity(userServer);
		service.put(entity);
		userServer.setKey(entity.getKey().getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#findByUser(com.google.appengine
	 * .api.datastore.Key)
	 */
	@Override
	public List<UserServer> findByUser(Long user) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(UserDAOImpl.ENTITY, user));

		PreparedQuery pq = service.prepare(q);

		List<UserServer> list = new ArrayList<UserServer>();

		for (Entity entity : pq.asIterable()) {
			list.add(fromEntity(entity));
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
	public UserServer get(Long key) throws EntityDoesNotExistsException {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		try {
			return fromEntity(service.get(KeyFactory.createKey(ENTITY, key)));
		} catch (EntityNotFoundException e) {
			throw new EntityDoesNotExistsException("UserServer with id %s not found", key.toString());
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jipdbs.core.model.dao.impl.UserServerDAO#findByUserAndServer(com.google
	 * .appengine.api.datastore.Key, com.google.appengine.api.datastore.Key)
	 */
	@Override
	public UserServer findByUserAndServer(Long user, Long server) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(UserDAOImpl.ENTITY, user));
		q.addFilter("server", FilterOperator.EQUAL, KeyFactory.createKey(ServerDAOImpl.ENTITY, server));
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();
		if (entity != null) {
			return fromEntity(entity);
		}
		return null;
	}

	@Override
	public List<UserServer> findByServer(Long server) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addFilter("server", FilterOperator.EQUAL, KeyFactory.createKey(ENTITY, server));

		PreparedQuery pq = service.prepare(q);

		List<UserServer> list = new ArrayList<UserServer>();

		for (Entity entity : pq.asIterable()) {
			list.add(fromEntity(entity));
		}

		return list;
	}

}
