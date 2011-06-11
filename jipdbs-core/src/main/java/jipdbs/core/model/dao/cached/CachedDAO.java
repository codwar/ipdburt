package jipdbs.core.model.dao.cached;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jipdbs.core.util.LocalCache;

public abstract class CachedDAO {

	private static final Logger log = Logger.getLogger(CachedDAO.class.getName());
	
	protected LocalCache cache;

	protected final Integer SEARCH_EXPIRE = 5;
	
	protected abstract void initializeCache();
	
	@SuppressWarnings("unchecked")
	protected Object getCachedList(String key, int[] count) {
		try {
			Map<String, Object> map = (Map<String, Object>) LocalCache.getInstance().get(key);
			if (map != null) {
				if (log.isLoggable(Level.FINE)) log.fine(map.toString());
				count[0] = (Integer) map.get("count");
				return map.get("list");
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected void putCachedList(String key, Object list, int[] count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("count", count[0]);
		LocalCache.getInstance().put(key, map, SEARCH_EXPIRE);
	}
	
}
