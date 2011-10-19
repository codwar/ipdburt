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
import iddb.core.model.dao.AliasDAO;

import java.util.Collection;
import java.util.List;

public class AliasDAOCached extends CachedDAO implements AliasDAO {

	private final AliasDAO impl;

	public AliasDAOCached(AliasDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	protected void initializeCache() {
		createCache("alias");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> findBySimilar(String query, int offset, int limit,
			int[] count) {
		String key = "gram" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findBySimilar(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public List<Alias> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		return impl.findByPlayer(player, offset, limit, count);
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
	 * @see iddb.core.model.dao.AliasDAO#booleanSearchByServer(java.lang.String, java.lang.Long, int, int, int[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> booleanSearchByServer(String query, Long serverkey, int offset, int limit, int[] count) {
		String key = "boos" + query + "S" + serverkey.toString() + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.booleanSearchByServer(query, serverkey, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.AliasDAO#booleanSearch(java.lang.String, int, int, int[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Alias> booleanSearch(String query, int offset, int limit,
			int[] count) {
		String key = "bool" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.booleanSearch(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}
}
