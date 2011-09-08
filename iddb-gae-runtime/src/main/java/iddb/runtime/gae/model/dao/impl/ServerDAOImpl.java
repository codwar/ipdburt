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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withPrefetchSize;
import iddb.core.model.Server;
import iddb.core.model.dao.ServerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ServerDAOImpl implements ServerDAO {

	public static final String ENTITY = "Server";

	private Server fromEntity(Entity entity) {
		Server server = new Server();
		server.setKey(entity.getKey().getId());
		server.setPluginVersion((String) entity.getProperty("pluginversion"));
		server.setCreated((Date) entity.getProperty("created"));
		server.setUpdated((Date) entity.getProperty("updated"));
		server.setAdminEmail(((Email) entity.getProperty("admin")).getEmail());
		server.setName((String) entity.getProperty("name"));
		server.setUid((String) entity.getProperty("uid"));
		server.setOnlinePlayers(((Long) entity.getProperty("players")).intValue());
		server.setAddress((String) entity.getProperty("ip"));
		server.setMaxLevel((Long) entity.getProperty("maxlevel"));
		server.setDirty((Boolean) entity.getProperty("dirty"));
		Long permission = (Long) entity.getProperty("permission");
		server.setPermission(permission != null ? permission.intValue() : 0);
		return server;
	}

	private Entity toEntity(Server server) {
		Entity entity = server.getKey() == null ? new Entity(ENTITY) : new Entity(KeyFactory.createKey(ENTITY, server.getKey()));
		entity.setProperty("updated", server.getUpdated());
		entity.setProperty("uid", server.getUid());
		entity.setProperty("ip", server.getAddress());
		entity.setProperty("dirty", server.getDirty());
		entity.setProperty("name", server.getName());
		entity.setUnindexedProperty("created", server.getCreated());
		entity.setUnindexedProperty("pluginversion", server.getPluginVersion());
		entity.setUnindexedProperty("maxlevel", server.getMaxLevel());		
		entity.setUnindexedProperty("players", server.getOnlinePlayers());
		entity.setUnindexedProperty("admin", new Email(server.getAdminEmail()));
		entity.setUnindexedProperty("permission", server.getPermission());
		return entity;
	}
	
	@Override
	public void save(Server server) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Entity entity = toEntity(server);
		service.put(entity);
		server.setKey(entity.getKey().getId());
	}

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addSort("name", SortDirection.ASCENDING);

		PreparedQuery pq = service.prepare(q);
		count[0] = pq.countEntities(withPrefetchSize(limit));

		List<Server> list = new ArrayList<Server>();

		for (Entity e : pq.asList(withOffset(offset).limit(limit)))
			list.add(fromEntity(e));

		return list;
	}

	@Override
	public Server findByUid(String uid) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query(ENTITY);
		q.addFilter("uid", FilterOperator.EQUAL, uid);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return fromEntity(entity);

		return null;
	}

	@Override
	public Server get(Long server) throws EntityDoesNotExistsException {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		
		try {
			Entity entity = service.get(KeyFactory.createKey(ENTITY, server));
			return fromEntity(entity);
		} catch (EntityNotFoundException e) {
			throw new EntityDoesNotExistsException("Server with id %s not found", server);
		}
		
	}
}
