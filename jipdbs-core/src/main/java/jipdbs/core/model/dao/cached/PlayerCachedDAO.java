package jipdbs.core.model.dao.cached;

import java.util.Collection;
import java.util.List;

import jipdbs.core.model.Player;
import jipdbs.core.model.dao.PlayerDAO;
import jipdbs.core.util.LocalCache;

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
	public void save(Player player, boolean commit) {
		impl.save(player, commit);
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
		Player p = (Player) cache.get("player-" + KeyFactory.keyToString(player));
		if (p != null) return p;
		p = impl.get(player);
		cache.put("player-" + KeyFactory.keyToString(player), p, 10);
		return p;
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
	public void save(Collection<Player> players, boolean commit) {
		for (Player player : players)
			save(player, commit);
	}

	@Override
	public void save(Player player) {
		save(player, true);
	}

	@Override
	public void save(Collection<Player> players) {
		save(players, true);
	}

	@Override
	public void cleanConnected(Key server) {
		impl.cleanConnected(server);
	}

	@Override
	public int countConnected(Key key) {
		return impl.countConnected(key);
	}
}
