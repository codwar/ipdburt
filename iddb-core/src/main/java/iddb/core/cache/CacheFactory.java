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
package iddb.core.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class CacheFactory {

	private static CacheFactory factory = null;
	private Map<String, Cache> cacheMap;
	
	private CacheFactory() {
		this.cacheMap = Collections.synchronizedMap(new HashMap<String, Cache>());
		this.cacheMap.put("default", getCacheImpl());
	}

	private Cache getCacheImpl() {
		return null;
	}

	private Cache getCacheImpl(String namespace) {
		return null;
	}
	
	public static CacheFactory getInstance() {
		if (factory == null) {
			factory = new CacheFactory();
		}
		return factory;
	}
	
	public void clearAll() {
		for (Cache cache : this.cacheMap.values()) {
			cache.clear();
		}
	}
	
	public Cache getCache() {
		return this.cacheMap.get("default");
	}
	
	public Cache getCache(String namespace) {
		Cache cache = this.cacheMap.get(namespace);
		if (cache == null) {
			cache = getCacheImpl(namespace);
			this.cacheMap.put(namespace, cache);
		}
		return cache;
	}
	
}

