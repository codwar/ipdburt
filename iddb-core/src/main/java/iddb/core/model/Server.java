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
package iddb.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.swing.text.MaskFormatter;

public class Server implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1356474862304251338L;

	private static final Long DEFAULT_MAX_LEVEL = 2L;

	private Long key;

	private String uid;
	private String name;
	private String adminEmail;
	private Date created;
	private Date updated;
	private int onlinePlayers;
	private String address;
	private String pluginVersion;
	private Long maxLevel;
	private Boolean dirty;
	private Integer permission;

	public Server() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String admin) {
		this.adminEmail = admin;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(name);
		b.append("[");
		b.append(uid);
		b.append("]");
		return b.toString();
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public Long getMaxLevel() {
		if (maxLevel == null) {
			return DEFAULT_MAX_LEVEL;
		}
		return maxLevel;
	}

	public void setMaxLevel(Long maxLevel) {
		this.maxLevel = maxLevel;
	}

	public Boolean getDirty() {
		if (dirty == null) {
			return true;
		}
		return dirty;
	}

	public void setDirty(Boolean dirty) {
		this.dirty = dirty;
	}

	//
	// public String getKeyString() {
	// return KeyFactory.keyToString(this.getKey());
	// }

	public Boolean getOffline() {
		Date today = new Date();
		if (this.updated != null) {
			long diff = today.getTime() - this.updated.getTime();
			return (diff / 86400000 >= 2);
		}
		return true;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

}