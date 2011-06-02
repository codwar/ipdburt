package jipdbs.core.model.dao.cached;

import java.util.Collection;
import java.util.List;

import jipdbs.core.model.Alias;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.util.LocalCache;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AliasCachedDAO implements AliasDAO {

	private static final LocalCache cache = LocalCache.getInstance();

	private final AliasDAO impl;

	public AliasCachedDAO(AliasDAO impl) {
		this.impl = impl;
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

	@Override
	public List<Alias> findByNickname(String query, int offset, int limit,
			int[] count) {
		return impl.findByNickname(query, offset, limit, count);
	}

	@Override
	public List<Alias> findByNGrams(String query, int offset, int limit,
			int[] count) {
		return impl.findByNGrams(query, offset, limit, count);
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

	@Override
	public List<Alias> findByServer(String query, int offset, int limit,
			int[] count) {
		return impl.findByServer(query, offset, limit, count);
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
