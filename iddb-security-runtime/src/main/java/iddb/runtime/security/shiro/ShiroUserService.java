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
package iddb.runtime.security.shiro;



import iddb.core.security.AnonymousUser;
import iddb.core.security.User;
import iddb.core.security.UserService;
import iddb.core.security.exceptions.InvalidAccountException;
import iddb.core.security.exceptions.InvalidCredentialsException;
import iddb.core.security.exceptions.UserLockedException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroUserService implements UserService {

	private static final Logger log = LoggerFactory.getLogger(ShiroUserService.class);
	
	/* (non-Javadoc)
	 * @see iddb.core.security.UserService#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			log.debug("User is authenticated");
			ShiroUser u = new ShiroUser(subject.getPrincipal().toString(), subject.hasRole("superadmin"));
			u.setEmail(u.getUsername());
			return u;
		}
		log.debug("User is Anonymous");
		return new AnonymousUser();
	}

	/* (non-Javadoc)
	 * @see iddb.core.security.UserService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public void authenticate(String username, String password)
			throws InvalidAccountException, InvalidCredentialsException,
			UserLockedException {
		log.debug("Perform authentication for user {}", username);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			log.debug("UnknownAccountException");
			throw new InvalidAccountException();
		} catch (IncorrectCredentialsException e) {
			log.debug("IncorrectCredentialsException");
			throw new InvalidCredentialsException();
		} catch (LockedAccountException e) {
			log.debug("LockedAccountException");
			throw new UserLockedException();
		} catch (AuthenticationException e) {
			log.error("AuthenticationException: {}", e.getMessage());
			throw new InvalidCredentialsException();
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.security.UserService#logout()
	 */
	@Override
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		log.debug("Logout user {}", subject.getPrincipal());
		subject.logout();
	}

}
