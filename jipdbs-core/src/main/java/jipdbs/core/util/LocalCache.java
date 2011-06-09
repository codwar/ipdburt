package jipdbs.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public final class LocalCache {

	private static final Logger log = Logger.getLogger(LocalCache.class.getName());

	private static LocalCache localManager = null;
	private Map<String, MemcacheService> cacheMap;
	
	private LocalCache() {
		this.cacheMap = Collections.synchronizedMap(new HashMap<String, MemcacheService>());
		this.cacheMap.put("default", MemcacheServiceFactory.getMemcacheService("default"));
	}

	public static LocalCache getInstance() {
		if (localManager == null) {
			localManager = new LocalCache();
		}
		return localManager;
	}

	private MemcacheService getCacheInstace(String namespace) {
		MemcacheService cache = this.cacheMap.get(namespace);
		if (cache == null) {
			cache = MemcacheServiceFactory.getMemcacheService(namespace);
			this.cacheMap.put(namespace, cache);
		}
		return cache;
	}

	public void clear(String namespace) {
		this.getCacheInstace(namespace).clearAll();
	}
	
	public void clear() {
		this.clear("default");
	}
	
	public void clearAll() {
		for (MemcacheService cache : this.cacheMap.values()) {
			cache.clearAll();
		}
	}
	/**
	 * Get object from cache
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.get("default", key);
	}

	/**
	 * Get object from cache
	 * @param key
	 * @return
	 */
	public Object get(String namespace, String key) {
		Object ob = this.getCacheInstace(namespace).get(key); 
		log.finest("Lookup for key " + key + " [" + Boolean.toString(ob != null) + "]");
		return ob;
	}
	
	/**
	 * Cache object with expiration time
	 * @param key
	 * @param value 
	 * @param expiration - Expiration time in minutes
	 */
	public void put(String key, Object value, Integer expiration) {
		this.put("default", key, value, expiration);
	}

	/**
	 * Cache object with expiration time
	 * @param namespace
	 * @param key
	 * @param value 
	 * @param expiration - Expiration time in minutes
	 */
	public void put(String namespace, String key, Object value, Integer expiration) {
		MemcacheService cache = this.getCacheInstace(namespace);
		synchronized (cache) {
			log.finest("Save key " + key);
			cache.put(key, value, Expiration.byDeltaSeconds(expiration * 60));
		}
	}

	/**
	 * Cache object with no expiration time
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		this.put("default", key, value);
	}
	
	/**
	 * Cache object with no expiration time
	 * @param key
	 * @param value
	 */
	public void put(String namespace, String key, Object value) {
		MemcacheService cache = this.getCacheInstace(namespace);
		synchronized (cache) {
			log.finest("Save key " + key);
			cache.put(key, value);
		}
	}
}
