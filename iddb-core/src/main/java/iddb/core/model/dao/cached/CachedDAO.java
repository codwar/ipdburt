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

import iddb.core.cache.Cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CachedDAO {

	private static final Logger log = LoggerFactory.getLogger(CachedDAO.class);
	
	protected Cache cache;
	
	protected final Integer SEARCH_EXPIRE = 5;
	
	protected abstract void initializeCache();
	
	@SuppressWarnings("unchecked")
	protected Object getCachedList(String key, int[] count) {
		try {
			Map<String, Object> map = (Map<String, Object>) cache.get(key);
			if (map != null) {
				if (log.isDebugEnabled()) log.debug(map.toString());
				count[0] = (Integer) map.get("count");
				return map.get("list");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected void putCachedList(String key, Object list, int[] count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("count", count[0]);
		cache.put(key, map, SEARCH_EXPIRE);
	}
	
}
