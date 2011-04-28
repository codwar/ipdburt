package jipdbs.data;

import java.util.Collection;
import java.util.List;

import jipdbs.util.LocalCache;

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
		cache.put(
				cacheKey(alias.getPlayer(), alias.getNickname(), alias.getIp()),
				alias);
	}

	@Override
	public Alias findByPlayerAndNicknameAndIp(Key player, String nickname,
			String ip) {
		Alias alias = (Alias) cache.get(cacheKey(player, nickname, ip));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndNicknameAndIp(player, nickname, ip);

		if (alias != null)
			cache.put(
					cacheKey(alias.getPlayer(), alias.getNickname(),
							alias.getIp()), alias);

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

	private String cacheKey(Key player, String nickname, String ip) {
		return "alias-" + KeyFactory.keyToString(player) + nickname + ip;
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
}
