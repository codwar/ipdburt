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

import java.util.List;

import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.User;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;
import iddb.web.security.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

	/**
	 * 
	 */
	public static final String SUBJECT = "user-service-subject";

	/**
	 * How long will be the cookie expire when remember is enabled
	 * (in seconds)
	 */
	public static final Integer COOKIE_EXPIRE_REMEMBER = 60 * 60 * 24 * 14;

	/**
	 * How long a session will be considered valid
	 * (in days)
	 */
	public static final Integer SESSION_LIFE = 14;
	
	public static final String SESSION_KEY = "user-session-key";
	
	/**
	 * Get current authenticated user or Anonymous
	 */
	public Subject getCurrentUser();

	/**
	 * Find user from session or session db
	 * @param request
	 * @return
	 */
	public Subject findUserSession(HttpServletRequest request);
	
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
	public Subject authenticate(HttpServletRequest request, HttpServletResponse response, String username, String password, boolean remember) throws InvalidAccountException, InvalidCredentialsException, UserLockedException;
	
	/**
	 * Logout the current authenticated user and remove any session trace
	 * @param request
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response);
	
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
	public boolean hasPermission(Long server, Integer level);
	
	/**
	 * Check if the user has level permission on any server
	 * @return
	 */
	public boolean hasAnyServer(Integer level);

	/**
	 * 
	 * @param server
	 * @return
	 */
	public Player getSubjectPlayer(Long server);
	
	/**
	 * Remove local data to avoid leeks
	 */
	public void cleanUp();

	/**
	 * @param level
	 * @return
	 */
	List<Server> listUserServers(Integer level);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(Long id) throws EntityDoesNotExistsException;
	
	/**
	 * 
	 * @param user
	 * @param server
	 * @return
	 */
	public Player getUserPlayer(User user, Long server);
	
}
