package jipdbs.data;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class ServerCachedDAO implements ServerDAO {
	
	private final ServerDAO impl;
	
	public ServerCachedDAO(ServerDAO impl) {
		this.impl = impl;
	}

	@Override
	public void save(Server server) {
		impl.save(server);
	}

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {
		return impl.findAll(offset, limit, count);
	}

	@Override
	public Server findByUid(String uid) {
		return impl.findByUid(uid);
	}

	@Override
	public Server get(Key server) throws EntityNotFoundException {
		return impl.get(server);
	}
}
