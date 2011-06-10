package jipdbs.core.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.core.model.Alias;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.util.Functions;
import jipdbs.core.util.NGrams;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class AliasDAOImpl implements AliasDAO {

	private static final Logger log = Logger.getLogger(AliasDAOImpl.class
			.getName());

	@Override
	public void save(Alias alias, boolean commit) {
		if (commit) {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();

			Entity entity = alias.toEntity();
			service.put(entity);
			alias.setKey(entity.getKey());
		}
	}

	@Override
	public Alias findByPlayerAndNickname(Key player, String nickname) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setAncestor(player);
		q.addFilter("nickname", FilterOperator.EQUAL, nickname);
		PreparedQuery pq = service.prepare(q);
		Entity entity = null;
		try {
			entity = pq.asSingleEntity();
		} catch (TooManyResultsException e) {
			log.severe("DUPLICATED:" + nickname + " for player " + player);
			List<Entity> list = pq.asList(withLimit(1));
			if (list.size() > 0) {
				entity = list.get(0);
			}
		}
		if (entity != null)
			return new Alias(entity);
		return null;
	}
	
	@Override
	public Alias findByPlayerAndNicknameAndIp(Key player, String nickname,
			String ip) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setAncestor(player);
		q.addFilter("nickname", FilterOperator.EQUAL, nickname);
		q.addFilter("ip", FilterOperator.EQUAL, Functions.ipToDecimal(ip));
		PreparedQuery pq = service.prepare(q);
		Entity entity = null;
		try {
			entity = pq.asSingleEntity();
		} catch (TooManyResultsException e) {
			log.severe("DUPLICATED:" + nickname + " with ip " + ip
					+ " for player " + player);
			List<Entity> list = pq.asList(withLimit(1));
			if (list.size() > 0) {
				entity = list.get(0);
			}
		}
		if (entity != null)
			return new Alias(entity);
		return null;
	}

	@Override
	public Alias getLastUsedAlias(Key player) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setAncestor(player);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);
		List<Entity> list = pq.asList(withLimit(1));
		if (list.size() == 0)
			return null;

		return new Alias(list.get(0));
	}

	@Override
	public List<Alias> findByNickname(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Collection<String> ngrams = new ArrayList<String>();
		ngrams.add(query.toLowerCase());

		Query q = new Query("PlayerAlias");
		q.addFilter("ngrams", FilterOperator.IN, ngrams);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity alias : list)
			result.add(new Alias(alias));

		return result;
	}

	@Override
	public List<Alias> findByNGrams(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Collection<String> ngrams = NGrams.ngrams(query);

		if (ngrams.size() == 0)
			return Collections.emptyList();

		Query q = new Query("PlayerAlias");
		q.addFilter("ngrams", FilterOperator.IN, ngrams);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit).limit(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity alias : list)
			result.add(new Alias(alias));

		return result;
	}

	@Override
	public List<Alias> findByPlayer(Key player, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setAncestor(player);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		if (count != null) count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity entity : list)
			result.add(new Alias(entity));

		return result;
	}

	@Override
	@Deprecated
	public List<Alias> findByIP(String query, int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");

		Long[] range = Functions.getIpRange(query);
		q.addFilter("ip", FilterOperator.GREATER_THAN_OR_EQUAL, range[0]);
		q.addFilter("ip", FilterOperator.LESS_THAN_OR_EQUAL, range[1]);
		q.addSort("ip", SortDirection.ASCENDING);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity entity : list)
			result.add(new Alias(entity));

		return result;
	}

	@Override
	public List<Alias> findByServer(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Key server = KeyFactory.stringToKey(query);
		Query q = new Query("Player").setKeysOnly();
		q.setAncestor(server);
		q.addSort("updated", SortDirection.DESCENDING);
		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Alias> result = new ArrayList<Alias>();
		for (Entity player : pq.asIterable(withLimit(limit).offset(offset))) {
			Alias alias = getLastUsedAlias(player.getKey());
			if (alias != null)
				result.add(alias);
		}
		return result;
	}

	@Override
	public void truncate() {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setKeysOnly();
		PreparedQuery pq = service.prepare(q);
		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : pq.asIterable()) {
			keys.add(entity.getKey());
		}
		service.delete(keys);
	}

	@Override
	public void save(Collection<Alias> aliasses, boolean commit) {
		for (Alias alias : aliasses)
			save(alias, commit);
	}

	@Override
	public void save(Alias alias) {
		save(alias, true);
	}

	@Override
	public void save(Collection<Alias> aliasses) {
		save(aliasses, true);
	}
}
