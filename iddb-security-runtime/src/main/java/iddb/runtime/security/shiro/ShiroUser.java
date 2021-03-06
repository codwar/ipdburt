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


public class ShiroUser implements User {

	private String username;
	private String email;
	private boolean superAdmin;
	
	public ShiroUser(String username, boolean isSuperAdmin) {
		this.username = username;
		this.superAdmin = isSuperAdmin;
	}
	
	/* (non-Javadoc)
	 * @see iddb.core.security.User#getUsername()
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/* (non-Javadoc)
	 * @see iddb.core.security.User#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	/* (non-Javadoc)
	 * @see iddb.core.security.User#isSuperAdmin()
	 */
	@Override
	public boolean isSuperAdmin() {
		return this.superAdmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
