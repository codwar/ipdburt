package jipdbs.core.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jipdbs.core.model.Penalty;
import jipdbs.core.model.Player;
import jipdbs.core.model.dao.PenaltyDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PenaltyDAOImpl implements PenaltyDAO {

	private Entity toEntity(Penalty penalty) {
		Entity entity = penalty.getKey() == null ? new Entity("Penalty", penalty.getPlayer().getKey()) : new Entity(penalty.getKey());
		entity.setProperty("updated", penalty.getUpdated());
		entity.setProperty("synced", penalty.getSynced());
		entity.setProperty("active", penalty.getActive());
		entity.setProperty("type", penalty.getType());
		entity.setUnindexedProperty("created", penalty.getCreated());
		entity.setUnindexedProperty("reason", penalty.getReason());
		entity.setUnindexedProperty("duration", penalty.getDuration());
		entity.setUnindexedProperty("admin", penalty.getAdmin());
		return entity;
	}

	private Penalty fromEntity(Entity entity) {
		Penalty penalty = new Penalty(new Player(entity.getParent()));
		penalty.setUpdated((Date) entity.getProperty("updated"));
		penalty.setCreated((Date) entity.getProperty("created"));
		penalty.setActive((Boolean) entity.getProperty("active"));
		penalty.setSynced((Boolean) entity.getProperty("synced"));
		penalty.setReason((String) entity.getProperty("reason"));
		penalty.setDuration((Integer) entity.getProperty("duration"));
		penalty.setAdmin(new Player((Key) entity.getProperty("admin")));
		penalty.setType((Integer) entity.getProperty("type"));
		return penalty;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#save(jipdbs.core.model.Penalty)
	 */
	@Override
	public void save(Penalty penalty) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Entity entity = toEntity(penalty);
		service.put(entity);
		penalty.setKey(entity.getKey());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#get(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public Penalty get(Key key) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		return fromEntity(ds.get(key));
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#findByPlayer(com.google.appengine.api.datastore.Key, int)
	 */
	@Override
	public List<Penalty> findByPlayer(Key player, int limit) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Penalty");
		q.setAncestor(player);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = ds.prepare(q);
		
		List<Penalty> list = new ArrayList<Penalty>();
		
		for (Entity entity : pq.asIterable(withLimit(limit))) {
			list.add(fromEntity(entity));
		}
		return list;
		
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#findByType(java.lang.Integer, int, int, int[])
	 */
	@Override
	public List<Penalty> findByType(Integer type, int offset, int limit, int[] count) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Penalty");
		q.addFilter("type", FilterOperator.EQUAL, type);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = ds.prepare(q);
		
		count[0] = pq.countEntities(withPrefetchSize(limit));
		
		List<Penalty> list = new ArrayList<Penalty>();
		
		for (Entity entity : pq.asIterable(withOffset(offset).limit(limit))) {
			list.add(fromEntity(entity));
		}
		return list;
		
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#findByPlayerAndType(com.google.appengine.api.datastore.Key, java.lang.Integer, int, int, int[])
	 */
	@Override
	public List<Penalty> findByPlayerAndType(Key player, Integer type, int offset, int limit, int[] count) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Penalty");
		q.setAncestor(player);
		q.addFilter("type", FilterOperator.EQUAL, type);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = ds.prepare(q);
		
		count[0] = pq.countEntities(withPrefetchSize(limit));
		
		List<Penalty> list = new ArrayList<Penalty>();
		
		for (Entity entity : pq.asIterable(withOffset(offset).limit(limit))) {
			list.add(fromEntity(entity));
		}
		return list;
		
	}

}
