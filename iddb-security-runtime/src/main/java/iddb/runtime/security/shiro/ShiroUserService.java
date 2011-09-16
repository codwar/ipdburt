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



import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import iddb.core.security.AnonymousUser;
import iddb.core.security.User;
import iddb.core.security.UserService;

public class ShiroUserService implements UserService {

	/* (non-Javadoc)
	 * @see iddb.core.security.UserService#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return new ShiroUser(subject.getPrincipal().toString(), subject.hasRole("superadmin"));
		} 
		return new AnonymousUser();
	}

}
