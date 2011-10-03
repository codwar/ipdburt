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
import iddb.core.model.User;
import iddb.core.model.UserServer;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.UserDAO;
import iddb.core.model.dao.UserServerDAO;
import iddb.core.util.PasswordUtils;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;
import iddb.web.security.service.CommonUserService;
import iddb.web.security.subject.Subject;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUserServiceImpl extends CommonUserService {

	private UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
	private UserServerDAO serverDAO = (UserServerDAO) DAOFactory.forClass(UserServerDAO.class);
	private PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
	
	private static Logger log = LoggerFactory.getLogger(DbUserServiceImpl.class);
	
	public DbUserServiceImpl() {
		log.debug("Initialize DbUserServiceImpl");
	}
	
	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	protected Subject doAuthenticate(HttpServletRequest request, String username,
			String password) throws InvalidAccountException,
			InvalidCredentialsException, UserLockedException {

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new InvalidCredentialsException();
		}
		
		User user;
		try {
			user = userDAO.get(username);
		} catch (EntityDoesNotExistsException e) {
			log.trace("User {} do not exists", username);
			throw new InvalidAccountException();
		}
		
		if (!PasswordUtils.checkPassword(password, user.getPassword())) {
			log.trace("User {}. Invalid password.", username);
			throw new InvalidCredentialsException();
		}

		Subject subject = new Subject();
		subject.setKey(user.getKey());
		subject.setLoginId(user.getLoginId());
		subject.setPassword(user.getPassword());
		subject.setRoles(user.getRoles());

		return subject;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#hasPermission(java.lang.Long)
	 */
	@Override
	public boolean hasPermission(Long server) {
		Subject subject = this.getCurrentUser();
		if (!subject.isAuthenticated()) return false;
		if (subject.isSuperAdmin()) return true;
		
		try {
			serverDAO.findByUserAndServer(subject.getKey(), server);
		} catch (EntityDoesNotExistsException e) {
			log.trace("UserServer {} do not exists for user {}", server.toString(), subject.getLoginId());
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#hasPersmission(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public boolean hasPersmission(Long server, Integer level) {
		Subject subject = this.getCurrentUser();
		if (!subject.isAuthenticated()) return false;
		if (subject.isSuperAdmin()) return true;
		
		UserServer userServer;
		try {
			userServer = serverDAO.findByUserAndServer(subject.getKey(), server);
		} catch (EntityDoesNotExistsException e) {
			log.trace("UserServer {} do not exists for user {}", server.toString(), subject.getLoginId());
			return false;
		}
		if (userServer.getPlayer() == null || userServer.getPlayer() == 0) {
			log.trace("No associated player for userid {}", subject.getLoginId());
			return false;
		}
		
		Player player;
		try {
			player = playerDAO.get(userServer.getPlayer());
		} catch (EntityDoesNotExistsException e) {
			log.trace("Player {} do not exists", userServer.getPlayer());
			return false;
		}
		if (player.getLevel() != null && player.getLevel() >= level) {
			return true;
		}
		return false;
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
		Subject subject = this.getCurrentUser();
		if (!subject.isAuthenticated()) return false;
		if (subject.isSuperAdmin()) return true;
		return serverDAO.existsAny(subject.getKey(), level);
	}

}
