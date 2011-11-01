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
package iddb.web.security.service.local;

import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.User;
import iddb.web.security.dao.Session;
import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;
import iddb.web.security.service.CommonUserService;
import iddb.web.security.subject.Subject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class LocalUserServiceImpl extends CommonUserService {

	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	protected Subject doAuthenticate(HttpServletRequest request, String username,
			String password) throws InvalidAccountException,
			InvalidCredentialsException, UserLockedException {
		if (username.equals("admin") && password.equals("admin")) {
			Subject s = new Subject();
			s.setLoginId("admin");
			s.setPassword("admin");
			s.setKey(1L);
			s.setRoles(new HashSet<String>(Arrays.asList(new String[]{"admin"})));
			//createUserSession(request, s);
			return s;
		} else if (username.equals("user") && password.equals("user")) {
			Subject s = new Subject();
			s.setLoginId("user");
			s.setPassword("user");
			s.setKey(2L);
			s.setRoles(new HashSet<String>(Arrays.asList(new String[]{"user"})));
			//createUserSession(request, s);
			return s;			
		}
		throw new InvalidCredentialsException();
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#hasPermission(java.lang.Long)
	 */
	@Override
	public boolean hasPermission(Long server) {
		return true;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#hasPersmission(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public boolean hasPermission(Long server, Integer level) {
		return true;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.CommonUserService#doLogout(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void doLogout(HttpServletRequest request) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#hasAnyServer(java.lang.Integer)
	 */
	@Override
	public boolean hasAnyServer(Integer level) {
		return false;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.CommonUserService#findSession(java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	protected Session findSession(String key, Long userId, String ip) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.CommonUserService#createSession(java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	protected void createSession(String key, Long userId, String ip) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.CommonUserService#removeSession(java.lang.String)
	 */
	@Override
	protected void removeSession(String key) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.CommonUserService#findUser(java.lang.Long)
	 */
	@Override
	protected Subject findUser(Long key) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#getSubjectPlayer(java.lang.Long)
	 */
	@Override
	public Player getSubjectPlayer(Long server) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#listUserServers(java.lang.Integer)
	 */
	@Override
	public List<Server> listUserServers(Integer level) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#getUser(java.lang.Long)
	 */
	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#getUserPlayer(iddb.core.model.User, java.lang.Long)
	 */
	@Override
	public Player getUserPlayer(User user, Long server) {
		// TODO Auto-generated method stub
		return null;
	}

}
