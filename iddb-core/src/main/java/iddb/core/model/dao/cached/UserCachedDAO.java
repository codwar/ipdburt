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

public class UserCachedDAO implements UserDAO {

	private UserDAO impl;
	
	public UserCachedDAO(UserDAO impl) {
		this.impl = impl;
	}
	
	@Override
	public void save(User user) {
		this.impl.save(user);
	}

	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		return this.impl.findAll(offset, limit, count);
	}

	@Override
	public User findByEmail(String email) {
		return this.impl.findByEmail(email);
	}

	@Override
	public User get(Long key) throws EntityDoesNotExistsException {
		return this.impl.get(key);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#authenticate(iddb.core.model.User, java.lang.String)
	 */
	@Override
	public boolean authenticate(User user, String password) {
		return this.impl.authenticate(user, password);
	}

}
