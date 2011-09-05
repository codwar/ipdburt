package jipdbs.core.model.dao.cached;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

import jipdbs.core.model.UserServer;
import jipdbs.core.model.dao.UserServerDAO;

public class UserServerCachedDAO implements UserServerDAO {

	private UserServerDAO impl;
	
	public UserServerCachedDAO(UserServerDAO impl) {
		this.impl = impl;
	}
	
	@Override
	public void save(UserServer userServer) {
		this.impl.save(userServer);
	}

	@Override
	public List<UserServer> findByUser(Key user) {
		return this.impl.findByUser(user);
	}

	@Override
	public UserServer get(Key userServer) throws EntityNotFoundException {
		return this.impl.get(userServer);
	}

	@Override
	public UserServer findByUserAndServer(Key user, Key server) {
		return this.impl.findByUserAndServer(user, server);
	}

	@Override
	public List<UserServer> findByServer(Key server) {
		return this.impl.findByServer(server);
	}

}
