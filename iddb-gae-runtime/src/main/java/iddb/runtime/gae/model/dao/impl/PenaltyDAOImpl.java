/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.runtime.gae.model.dao.impl;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import iddb.core.model.Penalty;
import iddb.core.model.dao.PenaltyDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class PenaltyDAOImpl implements PenaltyDAO {

	public static final String ENTITY = "Penalty";
	
	private static final Logger log = LoggerFactory.getLogger(PenaltyDAOImpl.class);
	
	private Entity toEntity(Penalty penalty) {
		Entity entity = penalty.getKey() == null ? new Entity(ENTITY, KeyFactory.createKey(PlayerDAOImpl.ENTITY, penalty.getPlayer())) : new Entity(KeyFactory.createKey(ENTITY, penalty.getKey()));
		entity.setProperty("updated", penalty.getUpdated());
		entity.setProperty("synced", penalty.getSynced());
		entity.setProperty("active", penalty.getActive());
		entity.setProperty("type", penalty.getType());
		entity.setUnindexedProperty("created", penalty.getCreated());
		entity.setUnindexedProperty("reason", penalty.getReason());
		entity.setUnindexedProperty("duration", penalty.getDuration());
		if (penalty.getAdmin() != null) {
			entity.setUnindexedProperty("admin", KeyFactory.createKey(PlayerDAOImpl.ENTITY, penalty.getAdmin()));	
		}
		return entity;
	}

	private Penalty fromEntity(Entity entity) {
		Penalty penalty = new Penalty();
		penalty.setPlayer(entity.getParent().getId());
		penalty.setUpdated((Date) entity.getProperty("updated"));
		penalty.setCreated((Date) entity.getProperty("created"));
		penalty.setActive((Boolean) entity.getProperty("active"));
		penalty.setSynced((Boolean) entity.getProperty("synced"));
		penalty.setReason((String) entity.getProperty("reason"));
		penalty.setDuration((Long) entity.getProperty("duration"));
		Key k = (Key) entity.getProperty("admin");
		if (k != null) penalty.setAdmin(k.getId());
		penalty.setType((Long) entity.getProperty("type"));
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
		penalty.setKey(entity.getKey().getId());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#get(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public Penalty get(Long key) throws EntityDoesNotExistsException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		try {
			Penalty p = fromEntity(ds.get(KeyFactory.createKey(ENTITY, key)));
			return p;
		} catch (EntityNotFoundException e) {
			throw new EntityDoesNotExistsException("Penalty with id %s was not found", key);
		}
	}

	@Override
	public List<Penalty> findByPlayer(Long player) {
		return this.findByPlayer(player, 1000);
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.PenaltyDAO#findByPlayer(com.google.appengine.api.datastore.Key, int)
	 */
	@Override
	public List<Penalty> findByPlayer(Long player, int limit) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
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
	public List<Penalty> findByType(Long type, int offset, int limit, int[] count) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query(ENTITY);
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
	public List<Penalty> findByPlayerAndType(Long player, Long type, int offset, int limit, int[] count) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
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

	@Override
	public void save(List<Penalty> list) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		List<Entity> entities = new ArrayList<Entity>();
		for (Penalty penalty : list) {
			entities.add(toEntity(penalty));
		}
		service.put(entities);
	}

	@Override
	public void delete(Penalty penalty) {
		if (penalty.getKey() != null) {
			DatastoreService service = DatastoreServiceFactory.getDatastoreService();
			service.delete(KeyFactory.createKey(ENTITY, penalty.getKey()));
		}
	}

	@Override
	public void delete(List<Penalty> list) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		List<Key> entities = new ArrayList<Key>();
		for (Penalty penalty : list) {
			if (penalty.getKey() != null) {
				entities.add(KeyFactory.createKey(ENTITY, penalty.getKey()));	
			}
		}
		service.delete(entities);
	}

	@Override
	public List<Penalty> findByPlayerAndTypeAndActive(Long player, Long type) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
		q.addFilter("type", FilterOperator.EQUAL, type);
		q.addFilter("active", FilterOperator.EQUAL, true);

		PreparedQuery pq = ds.prepare(q);
		List<Penalty> list = new ArrayList<Penalty>();
		for (Entity entity : pq.asIterable()) {
			list.add(fromEntity(entity));
		}
		return list;
	}
	
	@Override
	public List<Penalty> findByPlayerAndType(Long player, Long type) {
		int[] count = new int[1];
		return this.findByPlayerAndType(player, type, 0, 1000, count);
	}
	
}
