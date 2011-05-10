package jipdbs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public class LocalCache {

	private static final Logger log = Logger.getLogger(LocalCache.class
			.getName());

	private static LocalCache localManager = null;
	private static Cache cacheManager = null;

	// private final Integer CACHE_EXPIRATION = 300; // 5 minutes

	private LocalCache() {
		Map<String, Integer> props = new HashMap<String, Integer>();
		// never expire
		//props.put(GCacheFactory.EXPIRATION_DELTA, CACHE_EXPIRATION);
		try {
			cacheManager = CacheManager.getInstance().getCacheFactory()
					.createCache(props);
		} catch (CacheException e) {
			log.severe(e.getMessage());
		}
	}

	// FIXME thread unsafe.
	public static LocalCache getInstance() {
		if (localManager == null) {
			localManager = new LocalCache();
		}
		return localManager;
	}

	public void clear() {
		if (cacheManager != null) {
			cacheManager.clear();
		}
	}
	public Object get(String key) {
		if (cacheManager == null)
			return null;
		Object ob = cacheManager.get(key); 
		log.finest("Lookup for key " + key + " [" + Boolean.toString(ob != null) + "]");
		return ob;
	}

	public void put(String key, Object value) {
		if (cacheManager == null)
			return;
		log.finest("Save key " + key);
		cacheManager.put(key, value);
	}

}
