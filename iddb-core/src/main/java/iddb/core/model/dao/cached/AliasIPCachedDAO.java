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
import iddb.core.model.AliasIP;
import iddb.core.model.dao.AliasIPDAO;

import java.util.List;

public class AliasIPCachedDAO extends CachedDAO implements AliasIPDAO {

	private AliasIPDAO impl;
	
	public AliasIPCachedDAO(AliasIPDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	private String cacheKey(Long player, String ip) {
		return "key-" + player + "#" + ip;
	}
	
	@Override
	public void save(AliasIP alias) {
		impl.save(alias);
		if (alias.getKey() != null) {
			cache.put(cacheKey(alias.getPlayer(), alias.getIp()),alias);
		}
	}

	@Override
	public AliasIP findByPlayerAndIp(Long player, String ip) {
		AliasIP alias = (AliasIP) cache.get(cacheKey(player, ip));

		if (alias != null)
			return alias;

		alias = impl.findByPlayerAndIp(player, ip);

		if (alias != null)
			cache.put(
					cacheKey(alias.getPlayer(), alias.getIp()), alias);

		return alias;
	}

	@Override
	public List<AliasIP> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		return impl.findByPlayer(player, offset, limit, count);
	}

	@Override
	public List<AliasIP> findByIP(String query, int offset, int limit,
			int[] count) {
		return impl.findByIP(query, offset, limit, count);
	}

	@Override
	protected void initializeCache() {
		this.cache = CacheFactory.getInstance().getCache("aliasip");
	}

}
