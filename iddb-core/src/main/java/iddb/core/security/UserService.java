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
package iddb.core.security;

import iddb.core.security.exceptions.InvalidAccountException;
import iddb.core.security.exceptions.InvalidCredentialsException;
import iddb.core.security.exceptions.UserLockedException;

public interface UserService {

	public User getCurrentUser();

	public void authenticate(String username, String password) throws InvalidAccountException, InvalidCredentialsException, UserLockedException;
	
	public void logout();
	
}
