package jipdbs.core.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import jipdbs.core.model.UserServer;
import jipdbs.core.model.dao.UserServerDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserServerDAOImpl implements UserServerDAO {

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
