package jipdbs.core.model.dao.cached;

import java.util.List;

import jipdbs.core.cache.CacheFactory;
import jipdbs.core.model.AliasIP;
import jipdbs.core.model.dao.AliasIPDAO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AliasIPCachedDAO extends CachedDAO implements AliasIPDAO {

	private AliasIPDAO impl;
	
	public AliasIPCachedDAO(AliasIPDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	private String cacheKey(Key player, String ip) {
		return "aliasip-" + KeyFactory.keyToString(player) + ip;
	}
	
	@Override
	public void save(AliasIP alias) {
		impl.save(alias);
		if (alias.getKey() != null) {
			cache.put(cacheKey(alias.getPlayer(), alias.getIp()),alias);
		}
	}

	@Override
	public AliasIP findByPlayerAndIp(Key player, String ip) {
		AliasIP alias = (AliasIP) cache.get(cacheKey(player, ip));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndIp(player, ip);

		if (alias != null)
			cache.put(
					cacheKey(alias.getPlayer(), alias.getIp()), alias);

		return alias;
	}

	@Override
	public List<AliasIP> findByPlayer(Key player, int offset, int limit,
			int[] count) {
		return impl.findByPlayer(player, offset, limit, count);
	}

	@Override
	public List<AliasIP> findByIP(String query, int offset, int limit,
			int[] count) {
		return impl.findByIP(query, offset, limit, count);
	}

	@Override
	protected void initializeCache() {
		this.cache = CacheFactory.getInstance().getCache("aliasip");
	}

}
