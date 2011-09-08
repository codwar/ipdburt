package iddb.runtime.gae.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import iddb.core.model.Player;
import iddb.core.model.dao.PlayerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PlayerDAOImpl implements PlayerDAO {

	public static final String ENTITY = "Player";

	public Player fromEntity(Entity entity) {
		Player player = new Player();
		player.setKey(entity.getKey().getId());
		player.setServer(entity.getParent().getId());
		player.setCreated((Date) entity.getProperty("created"));
		player.setUpdated((Date) entity.getProperty("updated"));
		player.setGuid((String) entity.getProperty("guid"));
		player.setBanInfo((String) entity.getProperty("baninfo"));
		player.setBanInfoUpdated((Date) entity.getProperty("baninfoupdated"));
		try {
			player.setLevel((Long) entity.getProperty("level"));			
		} catch (Exception e) {
		}
		try {
			player.setClientId((Long) entity.getProperty("clientId"));
		} catch (Exception e) {
		}
		player.setNote((String) entity.getProperty("note"));
		player.setConnected((Boolean) entity.getProperty("connected"));
		player.setNickname((String) entity.getProperty("nickname"));
		player.setIp((String) entity.getProperty("ip"));
		return player;
	}

	public Entity toEntity(Player player) {
		Entity entity = player.getKey() == null ? new Entity(ENTITY, KeyFactory.createKey(ServerDAOImpl.ENTITY, player.getServer())) : new Entity(KeyFactory.createKey(ENTITY, player.getKey()));
		entity.setProperty("guid", player.getGuid());
		entity.setProperty("updated", player.getUpdated());
		entity.setProperty("server", KeyFactory.createKey(ServerDAOImpl.ENTITY, player.getServer()));
		entity.setProperty("baninfoupdated", player.getBanInfoUpdated());
		entity.setProperty("clientId", player.getClientId());
		entity.setProperty("connected", player.isConnected());
		entity.setUnindexedProperty("level", player.getLevel());
		entity.setUnindexedProperty("note", player.getNote());
		entity.setUnindexedProperty("baninfo", player.getBanInfo());
		entity.setUnindexedProperty("created", player.getCreated());
		entity.setUnindexedProperty("nickname", player.getNickname());
		entity.setUnindexedProperty("ip", player.getIp());
		return entity;
	}
	
	@Override
	public void save(Player player, boolean commit) {
		if (commit) {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();
			Entity entity = toEntity(player);
			service.put(entity);
			player.setKey(entity.getKey().getId());
		}
	}

	@Override
	public Player findByServerAndGuid(Long server, String guid) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(ServerDAOImpl.ENTITY, server));
		q.addFilter("guid", FilterOperator.EQUAL, guid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return fromEntity(entity);

		return null;
	}

	@Override
	public List<Player> findLatest(int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addSort("connected", SortDirection.DESCENDING);
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Player> players = new ArrayList<Player>();

		for (Entity entity : pq.asIterable(withOffset(offset).limit(limit)))
			players.add(fromEntity(entity));

		return players;
	}

	@Override
	public List<Player> findBanned(int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addFilter("baninfoupdated", FilterOperator.NOT_EQUAL, null);
		q.addSort("baninfoupdated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Player> players = new ArrayList<Player>();

		for (Entity entity : pq.asIterable(withOffset(offset).limit(limit)))
			players.add(fromEntity(entity));

		return players;
	}

	@Override
	public Player get(Long player) throws EntityDoesNotExistsException {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		try {
			return fromEntity(service.get(KeyFactory.createKey(ENTITY, player)));
		} catch (EntityNotFoundException e) {
			throw new EntityDoesNotExistsException("Player with id %s was not found", player.toString());
		}
	}

	@Override
	public void truncate() {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setKeysOnly();
		PreparedQuery pq = service.prepare(q);
		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : pq.asIterable()) {
			keys.add(entity.getKey());
		}
		service.delete(keys);
	}

	@Override
	public void cleanConnected(Long server) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(ServerDAOImpl.ENTITY, server));
		q.addFilter("connected", FilterOperator.EQUAL, new Boolean(true));
		PreparedQuery pq = service.prepare(q);

		List<Entity> commit = new ArrayList<Entity>();
		for (Entity entity : pq.asIterable()) {
			entity.setProperty("connected", new Boolean(false));
			commit.add(entity);
		}
		if (commit.size() > 0)
			service.put(commit);

	}

	@Override
	public void save(Collection<Player> players, boolean commit) {
		for (Player player : players)
			save(player, commit);
	}

	@Override
	public void save(Player player) {
		save(player, true);
	}

	@Override
	public void save(Collection<Player> players) {
		save(players, true);
	}

	@Override
	public int countConnected(Long server) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(ServerDAOImpl.ENTITY, server));
		q.addFilter("connected", FilterOperator.EQUAL, new Boolean(true));
		PreparedQuery pq = service.prepare(q);
		return pq.countEntities(withLimit(100));

	}

	@Override
	public List<Player> findByServer(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(ServerDAOImpl.ENTITY, query));
		q.addSort("connected", SortDirection.DESCENDING);
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Player> result = new ArrayList<Player>();
		for (Entity player : pq.asIterable(withLimit(limit).offset(offset))) {
			result.add(fromEntity(player));
		}
		return result;
	}

	@Override
	public List<Player> findByClientId(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addFilter("clientId", FilterOperator.EQUAL, Long.parseLong(query));
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Player> result = new ArrayList<Player>();
		for (Entity player : pq.asIterable(withLimit(limit).offset(offset))) {
			result.add(fromEntity(player));
		}
		return result;

	}
}
