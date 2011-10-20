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

import iddb.core.util.HashUtils;
import iddb.web.security.ThreadContext;
import iddb.web.security.dao.Session;
import iddb.web.security.exceptions.InvalidAccountException;
import iddb.web.security.exceptions.InvalidCredentialsException;
import iddb.web.security.exceptions.UserLockedException;
import iddb.web.security.subject.Subject;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class CommonUserService implements UserService {

	private static final Logger log = LoggerFactory.getLogger(CommonUserService.class);
	
	private static ThreadContext context = new ThreadContext();
	
	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#getCurrentUser(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Subject getCurrentUser() {
		return context.getSubject();
	}
	
	private void saveLocal(Subject subject) {
		context.setSubject(subject);
	}
	
	@Override
	public void cleanUp() {
		try {
			context.removeSubject();
		} catch (Exception e) {
		}
	}
	
	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#authenticate(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public Subject authenticate(HttpServletRequest request,
			HttpServletResponse response, 
			String username,
			String password, boolean remember) throws InvalidAccountException,
			InvalidCredentialsException, UserLockedException {
		Subject subject = doAuthenticate(request, username, password);
		createUserSession(request, response, subject, remember);
		return subject;
	}
	
	/* (non-Javadoc)
	 * @see iddb.web.security.service.UserService#findUserSession(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Subject findUserSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Subject s = null;
		if (session != null) {
			s = (Subject) session.getAttribute(SUBJECT);
		}
		if (s == null) {
			log.trace("Lookup cookie trace");
			String sessionKey = null;
			String sessionUser = null;
			sessionKey = getCookie(request.getCookies(), "iddb-k");
			if (sessionKey != null) {
				sessionUser = getCookie(request.getCookies(), "iddb-u");	
			}
			if (sessionKey != null && sessionUser != null) {
				log.trace("Found cookie trace");
				Session localSession = null;
				try {
					localSession = findSession(sessionKey, Long.parseLong(sessionUser), request.getRemoteAddr());
				} catch (NumberFormatException e) {
					log.error(e.getMessage());
				}
				if (localSession != null) {
					if (localSession.getCreated().before(DateUtils.addDays(new Date(), SESSION_LIFE))) {
						log.trace("Found valid session");
						s = findUser(localSession.getUserId());
						if (s != null) {
							session = request.getSession(true);
							session.setAttribute(SUBJECT, s);
							session.setAttribute(SESSION_KEY, localSession.getKey());
						}
					} else {
						log.trace("Session expired");
					}
				}
			} 
		} else {
			log.trace("Using subject from session");
		}
		if (s != null) saveLocal(s);
		return s;
	}
	
	private String getCookie(Cookie[] cookies, String key) {
		if (cookies == null || cookies.length == 0) {
			log.trace("No cookies sent");
			return null;
		}
		for (Cookie c : cookies) {
			log.trace("List cookie {} with value {}", c.getName(), c.getValue());
			if (key.equals(c.getName())) {
				return c.getValue();
			}
		}	
		return null;
	}
	
	protected void createUserSession(HttpServletRequest request, HttpServletResponse response, Subject subject, boolean persistent) {
		HttpSession session = request.getSession(true);
		session.setAttribute(UserService.SUBJECT, subject);
		saveLocal(subject);
		String sessionKey = HashUtils.generate(subject.getLoginId());
		session.setAttribute(UserService.SESSION_KEY, sessionKey);
		Cookie cookieKey = new Cookie("iddb-k", sessionKey);
		Cookie cookieUser = new Cookie("iddb-u", subject.getKey().toString());
		cookieKey.setPath(request.getContextPath() + "/");
		cookieUser.setPath(request.getContextPath() + "/");
		if (persistent) {
			cookieKey.setMaxAge(COOKIE_EXPIRE_REMEMBER);
			cookieUser.setMaxAge(COOKIE_EXPIRE_REMEMBER);
		} else {
			cookieKey.setMaxAge(-1);
			cookieUser.setMaxAge(-1);
		}
		response.addCookie(cookieKey);
		response.addCookie(cookieUser);
		
		log.trace("Create new session {}, {}, {}", new String[] { sessionKey, subject.getKey().toString(), request.getRemoteAddr() });
		createSession(sessionKey, subject.getKey(), request.getRemoteAddr());
		
	}
	
	protected void invalidateUserSession(HttpServletRequest request, HttpServletResponse response) {
		context.removeSubject();
		String sessionKey = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(UserService.SUBJECT);
			sessionKey = (String) session.getAttribute(UserService.SESSION_KEY);
			session.removeAttribute(UserService.SESSION_KEY);
		}
		// remove cookie
		Cookie cookie = new Cookie("iddb-u", "");
		cookie.setPath(request.getContextPath() + "/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		cookie = new Cookie("iddb-k", "");
		cookie.setPath(request.getContextPath() + "/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		
		if (sessionKey != null) {
			removeSession(sessionKey);
		}
	}
	
	/* (non-Javadoc)
	 * @see iddb.web.security.UserService#logout(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		doLogout(request);
		invalidateUserSession(request, response);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		context = null;
	}
	
	protected abstract Session findSession(String key, Long userId, String ip);
	
	protected abstract void createSession(String key, Long userId, String ip);
	
	protected abstract void removeSession(String key);
	
	protected abstract void doLogout(HttpServletRequest request);
	
	protected abstract Subject findUser(Long key);
	
	protected abstract Subject doAuthenticate(HttpServletRequest request, String username, String password) throws InvalidAccountException,	InvalidCredentialsException, UserLockedException;
	
}
