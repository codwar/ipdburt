package jipdbs.core.model.dao.cached;

import java.util.List;

import jipdbs.core.cache.CacheFactory;
import jipdbs.core.model.Server;
import jipdbs.core.model.dao.ServerDAO;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class ServerCachedDAO extends CachedDAO implements ServerDAO {

	private final ServerDAO impl;

	public ServerCachedDAO(ServerDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	public void save(Server server) {
		impl.save(server);
		cache.clear();
		cache.put(cacheKey(server.getUid()), server);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Server> findAll(int offset, int limit, int[] count) {
		String key = "server-all" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Server> servers = (List<Server>) getCachedList(key, count);
		if (servers != null) return servers;
		servers = impl.findAll(offset, limit, count);
		putCachedList(key, servers, count);
		return servers;
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

	@Override
	protected void initializeCache() {
		this.cache = CacheFactory.getInstance().getCache("server");
	}
}
