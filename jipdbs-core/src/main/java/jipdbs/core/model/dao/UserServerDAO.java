package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.UserServer;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface UserServerDAO {

	public abstract void save(UserServer userServer);

	public abstract List<UserServer> findByUser(Key user);

	public abstract List<UserServer> findByServer(Key server);
	
	public abstract UserServer get(Key userServer)
			throws EntityNotFoundException;

	public abstract UserServer findByUserAndServer(Key user, Key server);

}