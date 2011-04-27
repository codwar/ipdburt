package jipdbs.data;

import java.util.Collection;
import java.util.List;

import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PlayerCachedDAO implements PlayerDAO {

	private static final LocalCache cache = LocalCache.getInstance();

	private final PlayerDAO impl;

	public PlayerCachedDAO(PlayerDAO impl) {
		this.impl = impl;
	}

	@Override
	public void save(Player player) {
		impl.save(player);
		cache.put(cacheKey(player.getServer(), player.getGuid()), player);
	}

	@Override
	public Player findByServerAndGuid(Key server, String guid) {

		Player player = (Player) cache.get(cacheKey(server, guid));

		if (player != null)
			return player;

		player = impl.findByServerAndGuid(server, guid);

		if (player != null)
			cache.put(cacheKey(player.getServer(), player.getGuid()), player);

		return player;
	}

	@Override
	public List<Player> findLatest(int offset, int limit, int[] count) {
		return impl.findLatest(offset, limit, count);
	}

	@Override
	public List<Player> findBanned(int offset, int limit, int[] count) {
		return impl.findBanned(offset, limit, count);
	}

	@Override
	public Player get(Key player) throws EntityNotFoundException {
		return impl.get(player);
	}

	@Override
	public void truncate() {
		cache.clear();
		impl.truncate();
	}

	private String cacheKey(Key server, String guid) {
		return "player-" + KeyFactory.keyToString(server) + guid;
	}

	@Override
	public void save(Collection<Player> players) {
		for (Player player : players)
			save(player);
	}
}
