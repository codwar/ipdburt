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

import iddb.core.model.User;
import iddb.core.model.dao.UserDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public class UserDAOCached extends CachedDAO implements UserDAO {

	private UserDAO impl;
	
	public UserDAOCached(UserDAO impl) {
		super("user");
		this.impl = impl;
	}
	
	@Override
	public void save(User user) {
		this.impl.save(user);
		cacheUser(user);
	}

	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		String key = "all-" + Integer.toString(offset) + "L" + Integer.toString(limit);
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) getCachedList(key, count);
		if (users != null) return users;
		users = this.impl.findAll(offset, limit, count);
		putCachedList(key, users, count);
		return users;
	}

	@Override
	public User get(Long key) throws EntityDoesNotExistsException {
		String k = "key+" + key.toString();
		User u = (User) cacheGet(k);
		if (u != null) return u;
		u = this.impl.get(key);
		if (u != null) cacheUser(u);
		return u;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#get(java.lang.String)
	 */
	@Override
	public User get(String loginId) throws EntityDoesNotExistsException {
		User user = (User) cacheGet("login+" + loginId);
		if (user != null) return user;
		user = this.impl.get(loginId);
		cacheUser(user);
		return user;
		
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#set_password(iddb.core.model.User, java.lang.String)
	 */
	@Override
	public void change_password(User user) {
		this.impl.change_password(user);
		cacheUser(user);
	}
	
	private void cacheUser(User user) {
		cachePut("login+" + user.getLoginId(), user, 10);
		cachePut("key+" + user.getKey().toString(), user, 10);
	}

}
