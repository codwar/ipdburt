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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class CacheFactory {

	/**
	 * 
	 */
	private static final String CACHE_IMPL = "iddb.runtime.cache.impl.CacheImpl";

	private static final Logger log = LoggerFactory.getLogger(CacheFactory.class);
	
	private static CacheFactory factory = null;
	private Map<String, Cache> cacheMap;
	private boolean disabled = false;
	
	private CacheFactory() {
		this.cacheMap = Collections.synchronizedMap(new HashMap<String, Cache>());
	}

	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	private Cache getCacheImpl(String namespace) throws UnavailableCacheException {
		Object obj = null;
		try {
			Class impl = this.getClass().forName(CACHE_IMPL);
			Constructor<String> cons = impl.getConstructor(String.class);
			obj = cons.newInstance(new Object[] {namespace});
		} catch (ClassNotFoundException e) {
			log.warn(e.getMessage());
			throw new UnavailableCacheException();
		} catch (SecurityException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} catch (InstantiationException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} catch (InvocationTargetException e) {
			log.error(e.getMessage());
			throw new UnavailableCacheException();
		} 
		return (Cache) obj;
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
	
	/**
	 * Check if there is a cache implementation available
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public boolean verify() {
		try {
			@SuppressWarnings("unused")
			Class impl = this.getClass().forName(CACHE_IMPL);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public Cache getCache() throws UnavailableCacheException {
		return getCache("default");
	}
	
	public Cache getCache(String namespace) throws UnavailableCacheException {
		if (disabled) throw new UnavailableCacheException();
		Cache cache = this.cacheMap.get(namespace);
		if (cache == null) {
			try {
				cache = getCacheImpl(namespace);
			} catch (UnavailableCacheException e) {
				log.warn("Cache system is unavailable");
				disabled = true;
				throw e;
			}
			this.cacheMap.put(namespace, cache);
		}
		return cache;
	}

}

