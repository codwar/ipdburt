package jipdbs.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PlayerDAO {

	private Entity map(Player player) {

		Entity entity = player.getKey() == null ? new Entity("Player")
				: new Entity(player.getKey());

		entity.setProperty("baninfo", player.getBanInfo());
		entity.setProperty("created", player.getCreated());
		entity.setProperty("guid", player.getGuid());
		entity.setProperty("server", player.getServer());
		entity.setProperty("updated", player.getUpdated());

		return entity;
	}

	private Player map(Entity entity) {

		Player player = new Player();

		player.setKey(entity.getKey());
		player.setCreated((Date) entity.getProperty("created"));
		player.setUpdated((Date) entity.getProperty("updated"));
		player.setGuid((String) entity.getProperty("guid"));
		player.setServer((Key) entity.getProperty("server"));
		player.setBanInfo((String) entity.getProperty("baninfo"));

		return player;
	}

	public void save(DatastoreService service, Player player) {
		Entity entity = map(player);
		service.put(entity);
		player.setKey(entity.getKey());
	}

	public Player findByServerAndGuid(DatastoreService service, Key server,
			String guid) {

		Query q = new Query("Player");
		q.addFilter("server", FilterOperator.EQUAL, server);
		q.addFilter("guid", FilterOperator.EQUAL, guid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return map(entity);

		return null;
	}

	public List<Player> findLatest(DatastoreService service) {
		Query q = new Query("Player");
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		List<Player> players = new ArrayList<Player>();

		for (Entity entity : pq.asIterable(FetchOptions.Builder.withLimit(50)))
			players.add(map(entity));

		return players;
	}

	public Player get(DatastoreService service, Key player)
			throws EntityNotFoundException {
		return map(service.get(player));
	}
}
