package jipdbs.core.model.dao.cached;

import java.util.Collection;
import java.util.List;

import jipdbs.core.model.Alias;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.util.LocalCache;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AliasCachedDAO extends CachedDAO implements AliasDAO {

	private final AliasDAO impl;

	public AliasCachedDAO(AliasDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	protected void initializeCache() {
		this.cache = LocalCache.getInstance();
	}
	
	@Override
	public void save(Alias alias, boolean commit) {
		impl.save(alias, commit);
		if (alias.getKey() != null) {
			cache.put(cacheKey(alias.getPlayer(), alias.getNickname()),alias);
		}
	}

	@Override
	public Alias findByPlayerAndNicknameAndIp(Key player, String nickname,
			String ip) {
		Alias alias = impl.findByPlayerAndNicknameAndIp(player, nickname, ip);
		return alias;
	}

	@Override
	public Alias getLastUsedAlias(Key player) {
		return impl.getLastUsedAlias(player);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> findByNickname(String query, int offset, int limit, int[] count) {
		String key = "alias-nick" + query + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByNickname(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> findByNGrams(String query, int offset, int limit,
			int[] count) {
		String key = "alias-gram" + query + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByNGrams(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public List<Alias> findByPlayer(Key player, int offset, int limit,
			int[] count) {
		return impl.findByPlayer(player, offset, limit, count);
	}

	@Override
	public List<Alias> findByIP(String query, int offset, int limit, int[] count) {
		return impl.findByIP(query, offset, limit, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> findByServer(String query, int offset, int limit, int[] count) {
		String key = "alias-server" + query + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByServer(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public void truncate() {
		cache.clear();
		impl.truncate();
	}

	private String cacheKey(Key player, String nickname) {
		return "alias-" + KeyFactory.keyToString(player) + nickname;
	}

	@Override
	public void save(Collection<Alias> aliasses, boolean commit) {
		for (Alias alias : aliasses)
			save(alias, commit);
	}

	@Override
	public void save(Alias alias) {
		save(alias, true);
	}

	@Override
	public void save(Collection<Alias> aliasses) {
		save(aliasses, true);
	}

	@Override
	public Alias findByPlayerAndNickname(Key player, String nickname) {
		Alias alias = (Alias) cache.get(cacheKey(player, nickname));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndNickname(player, nickname);

		if (alias != null)
			cache.put(cacheKey(alias.getPlayer(), alias.getNickname()), alias);

		return alias;
	}
}
