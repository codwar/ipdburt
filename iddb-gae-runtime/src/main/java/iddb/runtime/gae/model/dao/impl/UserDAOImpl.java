package iddb.runtime.gae.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import iddb.core.model.User;
import iddb.core.model.dao.UserDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserDAOImpl implements UserDAO {

	public static final String ENTITY = "User";
	
	private Entity toEntity(User user) {
		Entity entity = user.getKey() == null ? new Entity(ENTITY) : new Entity(KeyFactory.createKey(ENTITY, user.getKey()));
		entity.setProperty("email", user.getEmail());
		return entity;
	}
	
	private User fromEntity(Entity entity) {
		User user = new User();
		user.setKey(entity.getKey().getId());
		user.setEmail((String) entity.getProperty("email"));
		return user;
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#save(jipdbs.core.model.User)
	 */
	@Override
	public void save(User user) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = toEntity(user);
		service.put(entity);
		user.setKey(entity.getKey().getId());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#findAll(int, int, int[])
	 */
	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(ENTITY);
		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<User> list = new ArrayList<User>();

		for (Entity e : pq.asIterable(withOffset(offset).limit(limit)))
			list.add(fromEntity(e));

		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#findByEmail(java.lang.String)
	 */
	@Override
	public User findByEmail(String email) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(ENTITY);
		q.addFilter("email", FilterOperator.EQUAL, email);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return fromEntity(entity);

		return null;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#get(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public User get(Long key) throws EntityDoesNotExistsException {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		try {
			return fromEntity(service.get(KeyFactory.createKey(ENTITY, key)));
		} catch (EntityNotFoundException e) {
			throw new EntityDoesNotExistsException("User with id %s not found", key.toString());
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#authenticate(iddb.core.model.User, java.lang.String)
	 */
	@Override
	public boolean authenticate(User user, String password) {
		return false;
	}

}
