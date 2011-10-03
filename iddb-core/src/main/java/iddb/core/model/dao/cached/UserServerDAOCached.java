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

import iddb.core.model.UserServer;
import iddb.core.model.dao.UserServerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;


public class UserServerDAOCached extends CachedDAO implements UserServerDAO {

	private UserServerDAO impl;
	
	public UserServerDAOCached(UserServerDAO impl) {
		this.impl = impl;
		initializeCache();
	}
	
	@Override
	public void save(UserServer userServer) {
		this.impl.save(userServer);
	}

	@Override
	public List<UserServer> findByUser(Long user) {
		return this.impl.findByUser(user);
	}

	@Override
	public UserServer get(Long userServer) throws EntityDoesNotExistsException {
		return this.impl.get(userServer);
	}

	@Override
	public UserServer findByUserAndServer(Long user, Long server) throws EntityDoesNotExistsException {
		UserServer userServer = (UserServer) cacheGet("u-" + user.toString() + "s" + server.toString());
		if (userServer != null) return userServer;
		userServer = this.impl.findByUserAndServer(user, server);
		cachePut("u-" + user.toString() + "s" + server.toString(), userServer);
		return userServer;
	}

	@Override
	public List<UserServer> findByServer(Long server) {
		@SuppressWarnings("unchecked")
		List<UserServer> l = (List<UserServer>) cacheGet("list" + server.toString());
		if (l != null) return l;
		l = this.impl.findByServer(server);
		if (l.size() > 0) cachePut("list" + server.toString(), l);
		return l;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.cached.CachedDAO#initializeCache()
	 */
	@Override
	protected void initializeCache() {
		createCache("userserver");
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserServerDAO#findByPlayerAndServer(java.lang.Long, java.lang.Long)
	 */
	@Override
	public UserServer findByPlayerAndServer(Long player, Long server)
			throws EntityDoesNotExistsException {
		UserServer userServer = (UserServer) cacheGet("p-" + player.toString() + "s" + server.toString());
		if (userServer != null) return userServer;
		userServer = this.impl.findByPlayerAndServer(player, server);
		cachePut("p-" + player.toString() + "s" + server.toString(), userServer);
		return userServer;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserServerDAO#existsAny(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public Boolean existsAny(Long user, Integer level) {
		Boolean r = (Boolean) cacheGet("any" + user.toString() + "l" + level.toString());
		if (r != null) return r;
		r = this.impl.existsAny(user, level);
		cachePut("any" + user.toString() + "l" + level.toString(), r, 5);
		return r;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserServerDAO#listUserServers(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<UserServer> listUserServers(Long user, Integer level) {
		@SuppressWarnings("unchecked")
		List<UserServer> l = (List<UserServer>) cacheGet("liu" + user.toString() + "l" + level.toString());
		if (l != null) return l;
		l = this.impl.listUserServers(user, level);
		if (l.size() > 0) cachePut("liu" + user.toString() + "l" + level.toString(), l, 5);
		return l;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserServerDAO#findServerAdmins(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<UserServer> findServerAdmins(Long server, Integer level) {
		@SuppressWarnings("unchecked")
		List<UserServer> l = (List<UserServer>) cacheGet("lis" + server.toString() + "l" + level.toString());
		if (l != null) return l;
		l = this.impl.findServerAdmins(server, level);
		if (l.size() > 0) cachePut("lis" + server.toString() + "l" + level.toString(), l, 5);
		return l;
	}

}
