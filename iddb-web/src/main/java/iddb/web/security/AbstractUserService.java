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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public abstract class AbstractUserService implements UserService {

	private static ThreadContext context = new ThreadContext();
	
	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#getCurrentUser(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Subject getCurrentUser() {
		return context.getSubject();
	}
	
	protected void saveLocal(Subject subject) {
		context.setSubject(subject);
	}
	
	protected void removeLocal() {
		context.removeSubject();
	}
	
	protected void createUserSession(HttpServletRequest request, Subject subject) {
		HttpSession session = request.getSession(true);
		session.setAttribute(UserService.SUBJECT, subject);
		saveLocal(subject);
	}
	
	protected void invalidateUserSession(HttpServletRequest request) {
		context.removeSubject();
		HttpSession session = request.getSession(false);
		if (session != null) {
			Subject s = (Subject) session.getAttribute(UserService.SUBJECT);
			if (s != null) session.removeAttribute(UserService.SUBJECT);
		}		
	}
	
	
	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#logout(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void logout(HttpServletRequest request) {
		invalidateUserSession(request);
	}
	
}
