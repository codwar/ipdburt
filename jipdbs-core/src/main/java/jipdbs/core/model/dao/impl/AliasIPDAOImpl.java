package jipdbs.core.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.core.model.AliasIP;
import jipdbs.core.model.dao.AliasIPDAO;
import jipdbs.core.util.Functions;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class AliasIPDAOImpl implements AliasIPDAO {

	private static final Logger log = Logger.getLogger(AliasIPDAOImpl.class
			.getName());

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#save(jipdbs.core.model.AliasIP)
	 */
	@Override
	public void save(AliasIP alias) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Entity entity = alias.toEntity();
		service.put(entity);
		alias.setKey(entity.getKey());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByPlayerAndIp(com.google.appengine.api.datastore.Key, java.lang.String)
	 */
	@Override
	public AliasIP findByPlayerAndIp(Key player, String ip) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("AliasIP");
		q.setAncestor(player);
		q.addFilter("ip", FilterOperator.EQUAL, Functions.ipToDecimal(ip));
		
		PreparedQuery pq = service.prepare(q);
		Entity entity = null;
		try {
			entity = pq.asSingleEntity();
		} catch (TooManyResultsException e) {
			log.severe("DUPLICATED: ip " + ip
					+ " for player " + player);
			List<Entity> list = pq.asList(withLimit(1));
			if (list.size() > 0) {
				entity = list.get(0);
			}
		}
		if (entity != null)
			return new AliasIP(entity);
		return null;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByPlayer(com.google.appengine.api.datastore.Key, int, int, int[])
	 */
	@Override
	public List<AliasIP> findByPlayer(Key player, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("AliasIP");
		q.setAncestor(player);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<AliasIP> result = new ArrayList<AliasIP>();

		for (Entity entity : list)
			result.add(new AliasIP(entity));

		return result;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByIP(java.lang.String, int, int, int[])
	 */
	@Override
	public List<AliasIP> findByIP(String query, int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("AliasIP");

		Long[] range = Functions.getIpRange(query);
		q.addFilter("ip", FilterOperator.GREATER_THAN_OR_EQUAL, range[0]);
		q.addFilter("ip", FilterOperator.LESS_THAN_OR_EQUAL, range[1]);
		q.addSort("ip", SortDirection.ASCENDING);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<AliasIP> result = new ArrayList<AliasIP>();

		for (Entity entity : list)
			result.add(new AliasIP(entity));

		return result;
	}

}
