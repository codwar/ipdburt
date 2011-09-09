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
import iddb.core.model.Alias;
import iddb.core.model.dao.AliasDAO;
import iddb.core.util.NGrams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

public class AliasDAOImpl implements AliasDAO {

	public static final String ENTITY = "PlayerAlias";
	
	private static final Logger log = LoggerFactory.getLogger(AliasDAOImpl.class);

	@SuppressWarnings("unchecked")
	private Alias fromEntity(Entity entity) {
		Alias alias = new Alias();
		alias.setKey(entity.getKey().getId());
		alias.setPlayer(entity.getParent().getId());
		alias.setServer(((Key) entity.getProperty("server")).getId());
		alias.setCreated((Date) entity.getProperty("created"));
		alias.setUpdated((Date) entity.getProperty("updated"));
		alias.setCount((Long) entity.getProperty("count"));
		alias.setNickname((String) entity.getProperty("nickname"));
		alias.setNgrams((Collection<String>) entity.getProperty("ngrams"));
		return alias;
	}

	private Entity toEntity(Alias alias) {
		Entity entity = alias.getKey() == null ? 
				new Entity(ENTITY, KeyFactory.createKey(PlayerDAOImpl.ENTITY, alias.getPlayer())) : 
					new Entity(KeyFactory.createKey(ENTITY, alias.getKey()));
		entity.setProperty("updated", alias.getUpdated());
		entity.setProperty("nickname", alias.getNickname());
		entity.setProperty("ngrams", alias.getNgrams());
		entity.setProperty("player", KeyFactory.createKey(PlayerDAOImpl.ENTITY, alias.getPlayer()));
		entity.setProperty("server", KeyFactory.createKey(ServerDAOImpl.ENTITY, alias.getServer()));
		entity.setUnindexedProperty("created", alias.getCreated());
		entity.setUnindexedProperty("count", alias.getCount());
		return entity;
	}

	@Override
	public void save(Alias alias, boolean commit) {
		if (commit) {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();
			Entity entity = toEntity(alias);
			service.put(entity);
			alias.setKey(entity.getKey().getId());
		}
	}

	@Override
	public Alias findByPlayerAndNickname(Long player, String nickname) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
		q.addFilter("nickname", FilterOperator.EQUAL, nickname);
		PreparedQuery pq = service.prepare(q);
		Entity entity = null;
		try {
			entity = pq.asSingleEntity();
		} catch (TooManyResultsException e) {
			log.warn("DUPLICATED: {} for player {}", nickname, player.toString());
			List<Entity> list = pq.asList(withLimit(1));
			if (list.size() > 0) {
				entity = list.get(0);
			}
		}
		if (entity != null)
			return fromEntity(entity);
		return null;
	}

	@Override
	public List<Alias> findByNickname(String query, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Collection<String> ngrams = new ArrayList<String>();
		ngrams.add(query.toLowerCase());

		Query q = new Query(ENTITY);
		q.addFilter("ngrams", FilterOperator.IN, ngrams);
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity alias : list)
			result.add(fromEntity(alias));

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

		Query q = new Query(ENTITY);
		q.addFilter("ngrams", FilterOperator.IN, ngrams);

		PreparedQuery pq = service.prepare(q);

		count[0] = pq.countEntities(withPrefetchSize(limit).limit(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity alias : list)
			result.add(fromEntity(alias));

		return result;
	}

	@Override
	public List<Alias> findByPlayer(Long player, int offset, int limit,
			int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("PlayerAlias");
		q.setAncestor(KeyFactory.createKey(PlayerDAOImpl.ENTITY, player));
		q.addSort("updated", SortDirection.DESCENDING);

		PreparedQuery pq = service.prepare(q);

		if (count != null) count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Entity> list = pq.asList(withLimit(limit).offset(offset));

		List<Alias> result = new ArrayList<Alias>();

		for (Entity entity : list)
			result.add(fromEntity(entity));

		return result;
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
