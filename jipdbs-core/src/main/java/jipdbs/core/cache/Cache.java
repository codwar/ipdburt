package jipdbs.core.cache;

import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public final class Cache {

	private static final Logger log = Logger.getLogger(Cache.class.getName());

	private MemcacheService service;
	
	public Cache() {
		this.service = MemcacheServiceFactory.getMemcacheService("default");
	}

	public Cache(String namespace) {
		this.service = MemcacheServiceFactory.getMemcacheService(namespace);
	}

	public void clear() {
		service.clearAll();
	}
	
	/**
	 * Get object from cache
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		Object ob = this.service.get(key); 
		log.finest("Lookup for key " + key + " [" + Boolean.toString(ob != null) + "]");
		return ob;
	}

	/**
	 * Cache object with expiration time
	 * @param key
	 * @param value 
	 * @param expiration - Expiration time in minutes
	 */
	public synchronized void put(String key, Object value, Integer expiration) {
		log.finest("Save key " + key);
		this.service.put(key, value, Expiration.byDeltaSeconds(expiration * 60));
	}

	/**
	 * Cache object with no expiration time
	 * @param key
	 * @param value
	 */
	public synchronized void put(String key, Object value) {
		log.finest("Save key " + key);
		this.service.put(key, value);
	}
	
}
