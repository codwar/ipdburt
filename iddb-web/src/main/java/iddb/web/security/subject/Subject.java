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
package iddb.web.security.subject;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import iddb.core.model.Player;
import iddb.core.model.User;

public class Subject extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8553739759968034511L;

	private String screenName;
	private Map<String, Player> serverPlayer;
	
	public Subject() {
		super();
		serverPlayer = new HashMap<String, Player>();
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	/* (non-Javadoc)
	 * @see iddb.core.model.User#setLoginId(java.lang.String)
	 */
	@Override
	public void setLoginId(String loginId) {
		super.setLoginId(loginId);
		String[] p = StringUtils.split(loginId, "@");
		this.screenName = p[0];
	}
	public boolean hasRole(String rolename) {
		return this.getRoles().contains(rolename);
	}
	
	public boolean isAuthenticated() {
		return true;
	}

	/**
	 * Convenient method to see if use has admin role
	 * @return
	 */
	public boolean isSuperAdmin() {
		return hasRole("admin");
	}

	public Map<String, Player> getServerPlayer() {
		return serverPlayer;
	}

	public void setServerPlayer(Map<String, Player> serverPlayer) {
		this.serverPlayer = serverPlayer;
	}
	
}
