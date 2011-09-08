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
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import iddb.core.model.AliasIP;
import iddb.core.model.dao.AliasIPDAO;
import iddb.core.util.Functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class AliasIPDAOImpl implements AliasIPDAO {

	public static final String ENTITY = "AliasIP";
	
	private static final Logger log = LoggerFactory.getLogger(AliasIPDAOImpl.class);

	private AliasIP fromEntity(Entity entity) {
		AliasIP alias = new AliasIP();
		alias.setKey(entity.getKey().getId());
		alias.setPlayer(((Key) entity.getParent()).getId());
		alias.setCreated((Date) entity.getProperty("created"));
		alias.setUpdated((Date) entity.getProperty("updated"));
		alias.setCount((Long) entity.getProperty("count"));
		alias.setIp((String) Functions.decimalToIp((Long) entity.getProperty("ip")));
		return alias;
	}

	private Entity toEntity(AliasIP alias) {
		Entity entity = alias.getKey() == null ? new Entity(ENTITY,KeyFactory.createKey(PlayerDAOImpl.ENTITY, alias.getPlayer())) : new Entity(KeyFactory.createKey(ENTITY, alias.getKey()));
		entity.setProperty("updated", alias.getUpdated());
		entity.setProperty("ip", Functions.ipToDecimal(alias.getIp()));
		entity.setUnindexedProperty("created", alias.getCreated());
		entity.setUnindexedProperty("count", alias.getCount());
		return entity;
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#save(jipdbs.core.model.AliasIP)
	 */
	@Override
	public void save(AliasIP alias) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		Entity entity = toEntity(alias);
		service.put(entity);
		alias.setKey(entity.getKey().getId());
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByPlayerAndIp(com.google.appengine.api.datastore.Key, java.lang.String)
	 */
	@Override
	public AliasIP findByPlayerAndIp(Long player, String ip) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
		q.addFilter("ip", FilterOperator.EQUAL, Functions.ipToDecimal(ip));
		
		PreparedQuery pq = service.prepare(q);
		Entity entity = null;
		try {
			entity = pq.asSingleEntity();
		} catch (TooManyResultsException e) {
			log.warn("DUPLICATED: ip {} for player {}", ip, player);
			List<Entity> list = pq.asList(withLimit(1));
			if (list.size() > 0) {
				entity = list.get(0);
			}
		}
		if (entity != null)
			return fromEntity(entity);
		return null;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByPlayer(com.google.appengine.api.datastore.Key, int, int, int[])
	 */
	@Override
	public List<AliasIP> findByPlayer(Long player, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<AliasIP> result = new ArrayList<AliasIP>();

		for (Entity entity : list)
			result.add(fromEntity(entity));

		return result;
	}

	/* (non-Javadoc)
	 * @see jipdbs.core.model.dao.impl.AliasIPDAO#findByIP(java.lang.String, int, int, int[])
	 */
	@Override
	public List<AliasIP> findByIP(String query, int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);

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
			result.add(fromEntity(entity));

		return result;
	}

}
