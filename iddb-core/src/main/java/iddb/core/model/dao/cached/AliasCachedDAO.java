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

import iddb.core.cache.CacheFactory;
import iddb.core.model.Alias;
import iddb.core.model.dao.AliasDAO;

import java.util.Collection;
import java.util.List;

public class AliasCachedDAO extends CachedDAO implements AliasDAO {

	private final AliasDAO impl;

	public AliasCachedDAO(AliasDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	protected void initializeCache() {
		this.cache = CacheFactory.getInstance().getCache("alias");
	}
	
	@Override
	public void save(Alias alias, boolean commit) {
		impl.save(alias, commit);
		if (alias.getKey() != null) {
			cache.put(cacheKey(alias.getPlayer(), alias.getNickname()),alias);
		}
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
	public List<Alias> findByNGrams(String query, int offset, int limit,
			int[] count) {
		String key = "gram" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		List<Alias> aliasses = (List<Alias>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByNGrams(query, offset, limit, count);
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
	public Alias findByPlayerAndNickname(Long player, String nickname) {
		Alias alias = (Alias) cache.get(cacheKey(player, nickname));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndNickname(player, nickname);

		if (alias != null)
			cache.put(cacheKey(alias.getPlayer(), alias.getNickname()), alias);

		return alias;
	}
}
