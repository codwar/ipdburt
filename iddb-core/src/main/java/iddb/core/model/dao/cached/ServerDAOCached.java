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
package iddb.core.model.dao.cached;

import iddb.core.model.Server;
import iddb.core.model.dao.ServerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Date;
import java.util.List;

public class ServerDAOCached extends CachedDAO implements ServerDAO {

	private final ServerDAO impl;

	public ServerDAOCached(ServerDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	@Override
	public void save(Server server) {
		if (server.getKey() == null) {
			// if the server is new, we clear the cache for the listed servers
			cacheClear();
		}
		impl.save(server);
		cachePut(cacheKey(server.getUid()), server);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Server> findAll(int offset, int limit, int[] count) {
		String key = "all" + Integer.toString(offset) + "L"
				+ Integer.toString(limit);
		List<Server> servers = (List<Server>) getCachedList(key, count);
		if (servers != null)
			return servers;
		servers = impl.findAll(offset, limit, count);
		putCachedList(key, servers, count);
		return servers;
	}

	@Override
	public Server findByUid(String uid) {

		Server server = (Server) cacheGet(cacheKey(uid));

		if (server != null)
			return server;

		server = impl.findByUid(uid);

		if (server != null)
			cachePut(cacheKey(server.getUid()), server);

		return server;
	}

	@Override
	public Server get(Long server) throws EntityDoesNotExistsException {
		return impl.get(server);
	}

	private String cacheKey(String uid) {
		return "key-" + uid;
	}

	@Override
	protected void initializeCache() {
		createCache("server");
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.ServerDAO#listNotUpdatedSince(java.util.Date)
	 */
	@Override
	public List<Server> listNotUpdatedSince(Date date) {
		return impl.listNotUpdatedSince(date);
	}
}
