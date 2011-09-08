/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB.
 *
 *   IPDB is free software: you can redistribute it and/or modify
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
 *   along with UrlResolver. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.runtime.gae.cache.impl;

import iddb.core.cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheImpl implements Cache {

	private static final Logger log = LoggerFactory.getLogger(CacheImpl.class);

	private MemcacheService service;
	
	public CacheImpl() {
		this.service = MemcacheServiceFactory.getMemcacheService("default");
	}

	public CacheImpl(String namespace) {
		this.service = MemcacheServiceFactory.getMemcacheService(namespace);
	}
	
	/* (non-Javadoc)
	 * @see iddb.core.cache.Cache#clear()
	 */
	@Override
	public void clear() {
		service.clearAll();
	}

	/* (non-Javadoc)
	 * @see iddb.core.cache.Cache#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		Object ob = this.service.get(key); 
		log.debug("Lookup for key " + key + " [" + Boolean.toString(ob != null) + "]");
		return ob;
	}

	/* (non-Javadoc)
	 * @see iddb.core.cache.Cache#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(String key, Object value) {
		log.debug("Save key " + key);
		this.service.put(key, value);
	}

	/* (non-Javadoc)
	 * @see iddb.core.cache.Cache#put(java.lang.String, java.lang.Object, java.lang.Integer)
	 */
	@Override
	public void put(String key, Object value, Integer expiration) {
		log.debug("Save key " + key);
		this.service.put(key, value, Expiration.byDeltaSeconds(expiration * 60));
	}

}
