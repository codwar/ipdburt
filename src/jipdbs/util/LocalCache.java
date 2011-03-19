package jipdbs.util;

import java.util.Collections;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public class LocalCache {
	
	private static final Logger log = Logger.getLogger(LocalCache.class.getName());
	
	private static LocalCache localManager = null;
	private static Cache cacheManager = null;

	private LocalCache() {
		try {
			cacheManager = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
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
		if (cacheManager == null) return null;
		log.finest("Lookup for key " + key);
		return cacheManager.get(key);
	}
	
	public void put(String key, Object value) {
		if (cacheManager == null) return;
		log.finest("Save key " + key);
		cacheManager.put(key, value);
	}
}
