package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.User;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface UserDAO {

	public abstract void save(User user);

	public abstract List<User> findAll(int offset, int limit, int[] count);

	public abstract User findByEmail(String email);

	public abstract User get(Key User) throws EntityNotFoundException;

}