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
package iddb.web.security;

import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;

import javax.servlet.http.HttpServletRequest;

public class LocalUserServiceImpl extends AbstractUserService {

	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public Subject authenticate(HttpServletRequest request, String username,
			String password) throws InvalidAccountException,
			InvalidCredentialsException, UserLockedException {
		if (username.equals("admin") && password.equals("admin")) {
			Subject s = new Subject();
			s.setLoginId("admin");
			s.setPassword("admin");
			s.setKey(1L);
			s.setSuperAdmin(true);
			createUserSession(request, s);
			return s;
		}
		throw new InvalidCredentialsException();
	}

}
