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

import iddb.core.model.Alias;
import iddb.core.model.Player;
import iddb.core.model.dao.AliasDAO;
import iddb.core.util.Functions;
import iddb.core.util.HashUtils;

import java.util.Collection;
import java.util.List;

public class AliasDAOCached extends CachedDAO implements AliasDAO {

	private final AliasDAO impl;

	public AliasDAOCached(AliasDAO impl) {
		super("alias");
		this.impl = impl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> findByNickname(String query, int offset, int limit, int[] count) {
		String key = "nick" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByNickname(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public List<Player> findBySimilar(String query, Long server, int offset, int limit,
			int[] count) {
		String key;
		if (server != null) {
			key = "gram" + query + "|S" + Long.toString(server) +  "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		} else {
			key = "gram" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		}
		@SuppressWarnings("unchecked")
		List<Player> aliasses = (List<Player>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findBySimilar(query, server, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public List<Alias> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		String key = "fbp" + Long.toString(player) + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByPlayer(player, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	private String cacheKey(Long player, String nickname) {
		return "key-" + player.toString() + "#" + nickname;
	}

	@Override
	public void save(Alias alias) {
		impl.save(alias);
		if (alias.getKey() != null) {
			cachePut(cacheKey(alias.getPlayer(), alias.getNickname()),alias);
		}
	}

	@Override
	public void save(Collection<Alias> aliasses) {
		for (Alias alias : aliasses) save(alias);
	}

	@Override
	public Alias findByPlayerAndNickname(Long player, String nickname) {
		Alias alias = (Alias) cacheGet(cacheKey(player, nickname));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndNickname(player, nickname);

		if (alias != null)
			cachePut(cacheKey(alias.getPlayer(), alias.getNickname()), alias);

		return alias;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.AliasDAO#findBySimilar(java.lang.String[], java.lang.Long, int, int, int[])
	 */
	@Override
	public List<Player> findBySimilar(String[] query, Long server,
			int offset, int limit, int[] count) {
		String key;
		String queryKey = HashUtils.getSHA1Hash(Functions.join(query, "_"));
		if (server != null) {
			key = "gram" + queryKey + "|S" + Long.toString(server) +  "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		} else {
			key = "gram" + queryKey + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		}
		@SuppressWarnings("unchecked")
		List<Player> aliasses = (List<Player>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findBySimilar(query, server, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

}
