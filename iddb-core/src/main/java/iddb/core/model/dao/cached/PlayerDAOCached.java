/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.core.model.dao.cached;

import iddb.core.model.Player;
import iddb.core.model.dao.PlayerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Collection;
import java.util.List;

public class PlayerDAOCached extends CachedDAO implements PlayerDAO {

	private final PlayerDAO impl;

	public PlayerDAOCached(PlayerDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	public void save(Player player, boolean commit) {
		impl.save(player, commit);
		cachePut(cacheKey(player.getServer(), player.getGuid()), player);
	}

	@Override
	public Player findByServerAndGuid(Long server, String guid) {

		Player player = (Player) cacheGet(cacheKey(server, guid));

		if (player != null)
			return player;

		player = impl.findByServerAndGuid(server, guid);

		if (player != null)
			cachePut(cacheKey(player.getServer(), player.getGuid()), player);

		return player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Player> findLatest(int offset, int limit, int[] count) {
		String key = "latest-" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findLatest(offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> findBanned(int offset, int limit, int[] count) {
		String key = "banned-" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findBanned(offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@Override
	public Player get(Long player) throws EntityDoesNotExistsException {
		String k = "puid-" + player.toString();
		Player p = (Player) cacheGet(k);
		if (p != null) return p;
		p = impl.get(player);
		cachePut(k, p, 10);
		return p;
	}

	private String cacheKey(Long server, String guid) {
		return "key-" + server.toString() + "#" + guid;
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
	public void cleanConnected(Long server) {
		impl.cleanConnected(server);
	}

	@Override
	public int countConnected(Long key) {
		return impl.countConnected(key);
	}

	@Override
	public List<Player> findByServer(Long server, int offset, int limit,
			int[] count) {
		String key = "player-server-" + server + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findByServer(server, offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@Override
	public List<Player> findByClientId(Long clientId, int offset, int limit,
			int[] count) {
		String key = "player-cid-" + clientId + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getCachedList(key, count);
		if (players != null) return players;
		players = impl.findByClientId(clientId, offset, limit, count);
		putCachedList(key, players, count);
		return players;
	}

	@Override
	protected void initializeCache() {
		createCache("player");
	}
	
}
