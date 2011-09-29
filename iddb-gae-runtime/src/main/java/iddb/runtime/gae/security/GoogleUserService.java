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
package iddb.runtime.gae.security;

import iddb.web.security.AnonymousUser;
import iddb.web.security.User;
import iddb.web.security.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.UserServiceFactory;

public final class GoogleUserService implements UserService {

	private static final Logger log = LoggerFactory.getLogger(GoogleUserService.class);
	
	public User getCurrentUser() {
		try {
			com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
			com.google.appengine.api.users.User gUser = userService.getCurrentUser();
			if (userService.isUserLoggedIn()) {
				User user = new GoogleUser(gUser.getEmail(), userService.isUserAdmin());
				return user;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new AnonymousUser();
	}
}

