package jipdbs.core.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class CacheFactory {

	private static CacheFactory factory = null;
	private Map<String, Cache> cacheMap;
	
	private CacheFactory() {
		this.cacheMap = Collections.synchronizedMap(new HashMap<String, Cache>());
		this.cacheMap.put("default", new Cache());
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
			cache = new Cache(namespace);
			this.cacheMap.put(namespace, cache);
		}
		return cache;
	}
	
}

