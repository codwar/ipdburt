package jipdbs.core.model.dao.cached;

import java.util.Collection;
import java.util.List;

import jipdbs.core.model.Player;
import jipdbs.core.model.dao.PlayerDAO;
import jipdbs.core.util.LocalCache;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PlayerCachedDAO extends CachedDAO implements PlayerDAO {

	private final PlayerDAO impl;

	public PlayerCachedDAO(PlayerDAO impl) {
		this.impl = impl;
		this.initializeCache();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Player> findLatest(int offset, int limit, int[] count) {
		String key = "player-latest-" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findLatest(offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> findBanned(int offset, int limit, int[] count) {
		String key = "player-banned-" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findBanned(offset, limit, count);
		putCachedList(key, players, count);
		return players;
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

	@Override
	public List<Player> findByServer(String query, int offset, int limit,
			int[] count) {
		String key = "player-server-" + query + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findByServer(query, offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@Override
	public List<Player> findByClientId(String query, int offset, int limit,
			int[] count) {
		String key = "player-cid-" + query + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findByClientId(query, offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@Override
	protected void initializeCache() {
		this.cache = LocalCache.getInstance();
	}
	
}
