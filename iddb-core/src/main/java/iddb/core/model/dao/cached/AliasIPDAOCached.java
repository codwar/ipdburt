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

import iddb.core.model.AliasIP;
import iddb.core.model.dao.AliasIPDAO;

import java.util.List;

public class AliasIPDAOCached extends CachedDAO implements AliasIPDAO {

	private AliasIPDAO impl;
	
	public AliasIPDAOCached(AliasIPDAO impl) {
		super("aliasip");
		this.impl = impl;
	}

	private String cacheKey(Long player, String ip) {
		return "key-" + player + "#" + ip;
	}
	
	@Override
	public void save(AliasIP alias) {
		impl.save(alias);
		if (alias.getKey() != null) {
			cachePut(cacheKey(alias.getPlayer(), alias.getIp()),alias);
		}
	}

	@Override
	public AliasIP findByPlayerAndIp(Long player, String ip) {
		AliasIP alias = (AliasIP) cacheGet(cacheKey(player, ip));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndIp(player, ip);

		if (alias != null)
			cachePut(
					cacheKey(alias.getPlayer(), alias.getIp()), alias);

		return alias;
	}

	@Override
	public List<AliasIP> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		String key = "fbp" + Long.toString(player) + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<AliasIP> aliasses = (List<AliasIP>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByPlayer(player, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

	@Override
	public List<AliasIP> findByIP(String query, int offset, int limit,
			int[] count) {
		String key = "fbi" + query + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<AliasIP> aliasses = (List<AliasIP>) getCachedList(key, count);
		if (aliasses != null) return aliasses;
		aliasses = impl.findByIP(query, offset, limit, count);
		putCachedList(key, aliasses, count);
		return aliasses;
	}

}
