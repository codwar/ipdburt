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
package iddb.core.model.dao;

import iddb.core.model.User;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public interface UserDAO {

	public abstract void save(User user);

	public abstract List<User> findAll(int offset, int limit, int[] count);

	public abstract User get(Long key) throws EntityDoesNotExistsException;
	
	public abstract User get(String loginId) throws EntityDoesNotExistsException;

	public abstract void change_password(User user);

	public abstract String findPassKey(String passkey, Integer hoursLimit);
	
	public abstract void savePassKey(String email, String passKey);

	/**
	 * @param hoursLimit
	 * @return
	 */
	public abstract Integer cleanUp(Integer hoursLimit);
	
}