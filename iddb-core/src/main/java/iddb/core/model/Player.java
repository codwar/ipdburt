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

public class Player implements Serializable {

	private static final long serialVersionUID = 6984528322229982497L;

	private Long key;
	private Long server;

	private String guid;
	private Date created;
	private Date updated;
	private Date banInfo;
	private Long clientId;
	private Long level;
	private String note;
	private Boolean connected;
	private String nickname;
	private String ip;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getServer() {
		return server;
	}

	public void setServer(Long server) {
		this.server = server;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public Date getBanInfo() {
		return banInfo;
	}

	public void setBanInfo(Date banInfo) {
		this.banInfo = banInfo;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long id) {
		this.clientId = id;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		return server.hashCode() ^ guid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Player))
			return false;

		if (obj == this)
			return true;

		Player other = (Player) obj;

		return server.equals(other.getServer()) && guid.equals(other.getGuid());
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(server);
		b.append("[");
		b.append(guid);
		b.append("]");
		return b.toString();
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean isConnected() {
		if (connected == null) {
			return false;
		}
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
