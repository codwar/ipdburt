package jipdbs.core.model.dao.cached;

import java.util.List;

import jipdbs.core.model.User;
import jipdbs.core.model.dao.UserDAO;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class UserCachedDAO implements UserDAO {

	private UserDAO impl;
	
	public UserCachedDAO(UserDAO impl) {
		this.impl = impl;
	}
	
	@Override
	public void save(User user) {
		this.impl.save(user);
	}

	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		return this.impl.findAll(offset, limit, count);
	}

	@Override
	public User findByEmail(String email) {
		return this.impl.findByEmail(email);
	}

	@Override
	public User get(Key User) throws EntityNotFoundException {
		return this.impl.get(User);
	}

}
