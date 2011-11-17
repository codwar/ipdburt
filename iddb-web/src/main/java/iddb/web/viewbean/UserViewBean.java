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
package iddb.web.viewbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserViewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113493724489314447L;

	private Long key;
	private String username;
	private List<UserServerViewBean> list;

	public UserViewBean() {
		list = new ArrayList<UserServerViewBean>();
	}
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<UserServerViewBean> getList() {
		return list;
	}
	public void setList(List<UserServerViewBean> list) {
		this.list = list;
	}
}
