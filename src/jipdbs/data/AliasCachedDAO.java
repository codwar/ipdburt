package jipdbs.data;

import java.util.List;

import com.google.appengine.api.datastore.Key;

public class AliasCachedDAO implements AliasDAO {

	private final AliasDAO impl;

	public AliasCachedDAO(AliasDAO impl) {
		this.impl = impl;
	}

	@Override
	public void save(Alias alias) {
		impl.save(alias);
	}

	@Override
	public Alias findByPlayerAndNicknameAndIp(Key player, String nickname,
			String ip) {
		return impl.findByPlayerAndNicknameAndIp(player, nickname, ip);
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
		impl.truncate();
	}
}
