package jipdbs.core.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.List;

import jipdbs.core.model.User;
import jipdbs.core.model.dao.UserDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserDAOImpl implements UserDAO {

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#save(jipdbs.core.model.User)
	 */
	@Override
	public void save(User user) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = user.toEntity();
		service.put(entity);
		user.setKey(entity.getKey());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#findAll(int, int, int[])
	 */
	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("User");
		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<User> list = new ArrayList<User>();

		for (Entity e : pq.asIterable(withOffset(offset).limit(limit)))
			list.add(new User(e));

		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#findByEmail(java.lang.String)
	 */
	@Override
	public User findByEmail(String email) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("User");
		q.addFilter("email", FilterOperator.EQUAL, email);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return new User(entity);

		return null;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.UserDAO#get(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public User get(Key User)
			throws EntityNotFoundException {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		return new User(service.get(User));
	}

}
