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
package iddb.web.security.service;

import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;
import iddb.web.security.subject.Subject;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

	/**
	 * 
	 */
	public static final String SUBJECT = "user-service-subject";

	/**
	 * Get current authenticated user or Anonymous
	 */
	public Subject getCurrentUser();

	/**
	 * Lookup user and authenticate
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 * @throws InvalidAccountException
	 * @throws InvalidCredentialsException
	 * @throws UserLockedException
	 */
	public Subject authenticate(HttpServletRequest request, String username, String password) throws InvalidAccountException, InvalidCredentialsException, UserLockedException;
	
	/**
	 * Logout the current authenticated user and remove any session trace
	 * @param request
	 */
	public void logout(HttpServletRequest request);
	
	/**
	 * Check if user has access to the specified server
	 * @param server
	 * @return
	 */
	public boolean hasPermission(Long server);
	
	/**
	 * Check if user has access to the specified server and if associated player has the indicated level
	 * @param server
	 * @param level
	 * @return
	 */
	public boolean hasPersmission(Long server, Integer level);
	
	/**
	 * Check if the user has level permission on any server
	 * @return
	 */
	public boolean hasAnyServer(Integer level);
	
}
