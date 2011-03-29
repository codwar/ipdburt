package jipdbs.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.List;

import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PlayerDAO {
	
	public void save(DatastoreService service, Player player) {
		Entity entity = player.toEntity();
		service.put(entity);
		player.setKey(entity.getKey());
	}

	public Player findByServerAndGuid(DatastoreService service, Key server,
			String guid) {

		Query q = new Query("Player");
		//q.addFilter("server", FilterOperator.EQUAL, server);
		q.setAncestor(server);
		q.addFilter("guid", FilterOperator.EQUAL, guid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return new Player(entity);

		return null;
	}

	public List<Player> findLatest(DatastoreService service, int offset,
			int limit, int[] count) {
		Query q = new Query("Player");
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Player> players = new ArrayList<Player>();

		for (Entity entity : pq.asIterable(withOffset(offset).limit(limit)))
			players.add(new Player(entity));

		return players;
	}

	public Player get(DatastoreService service, Key player)
			throws EntityNotFoundException {
		return new Player(service.get(player));
	}
	
	public void truncate(DatastoreService service) {
		Query q = new Query("Player");
		q.setKeysOnly();
		PreparedQuery pq = service.prepare(q);
		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : pq.asIterable()) {
			keys.add(entity.getKey());
		}
		service.delete(keys);
	}
}
