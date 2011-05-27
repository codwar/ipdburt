package jipdbs.core.data;

import java.util.List;

import jipdbs.core.util.LocalCache;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class ServerCachedDAO implements ServerDAO {

	private static final LocalCache cache = LocalCache.getInstance();

	private final ServerDAO impl;

	public ServerCachedDAO(ServerDAO impl) {
		this.impl = impl;
	}

	@Override
	public void save(Server server) {
		impl.save(server);
		cache.put(cacheKey(server.getUid()), server);
	}

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {
		return impl.findAll(offset, limit, count);
	}

	@Override
	public Server findByUid(String uid) {

		Server server = (Server) cache.get(cacheKey(uid));

		if (server != null)
			return server;

		server = impl.findByUid(uid);

		if (server != null)
			cache.put(cacheKey(server.getUid()), server);

		return server;
	}

	@Override
	public Server get(Key server) throws EntityNotFoundException {
		return impl.get(server);
	}

	private String cacheKey(String uid) {
		return "server-" + uid;
	}
}
