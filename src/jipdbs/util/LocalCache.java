package jipdbs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

public class LocalCache {

	private static final Logger log = Logger.getLogger(LocalCache.class
			.getName());

	private static LocalCache localManager = null;
	private static Cache cacheManager = null;

	private final Integer CACHE_EXPIRATION = 300; // 5 minutes

	private LocalCache() {
		Map<String, Integer> props = new HashMap<String, Integer>();
		props.put(GCacheFactory.EXPIRATION_DELTA, CACHE_EXPIRATION);
		try {
			cacheManager = CacheManager.getInstance().getCacheFactory()
					.createCache(props);
		} catch (CacheException e) {
			log.severe(e.getMessage());
		}
	}

	public static LocalCache getInstance() {
		if (localManager == null) {
			localManager = new LocalCache();
		}
		return localManager;
	}

	public Object get(String key) {
		if (cacheManager == null)
			return null;
		log.finest("Lookup for key " + key);
		return cacheManager.get(key);
	}

	public void put(String key, Object value) {
		if (cacheManager == null)
			return;
		log.finest("Save key " + key);
		cacheManager.put(key, value);
	}

}
