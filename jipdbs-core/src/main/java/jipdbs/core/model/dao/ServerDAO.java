package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.Server;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface ServerDAO {

	public abstract void save(Server server);

	public abstract List<Server> findAll(int offset, int limit, int[] count);

	public abstract Server findByUid(String uid);

	public abstract Server get(Key server) throws EntityNotFoundException;

}