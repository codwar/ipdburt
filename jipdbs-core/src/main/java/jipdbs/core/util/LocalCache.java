package jipdbs.core.util;

import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class LocalCache {

	private static final Logger log = Logger.getLogger(LocalCache.class.getName());

	private static LocalCache localManager = null;
	//private static Cache cacheManager = null;
	//private static ConcurrentHashMap<String, Object> cache = null;
	private static MemcacheService cache = null;
	
	private LocalCache() {
//		Map<String, Integer> props = new HashMap<String, Integer>();
//		try {
//			cacheManager = CacheManager.getInstance().getCacheFactory()
//					.createCache(props);
//		} catch (CacheException e) {
//			log.severe(e.getMessage());
//		}
//		cache = new ConcurrentHashMap<String, Object>(100);
		// disable cache
		cache = MemcacheServiceFactory.getMemcacheService(); 

	}

	public static LocalCache getInstance() {
		if (localManager == null) {
			localManager = new LocalCache();
		}
		return localManager;
	}

	public void clear() {
		if (cache != null) {
			cache.clearAll();
		}
	}
	
	/**
	 * Get object from cache
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		if (cache == null)
			return null;
		Object ob = cache.get(key); 
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
		if (cache == null)
			return;		
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
		if (cache == null)
			return;		
		synchronized (cache) {
			log.finest("Save key " + key);
			cache.put(key, value);
		}
	}
}
