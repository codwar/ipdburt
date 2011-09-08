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
package iddb.info;

import java.io.Serializable;
import java.util.Date;

public class PlayerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8609646730753062685L;

	private String name;
	private String guid;
	private Long clientId;
	private String ip;
	private Long level;
	private Date updated = new Date();
	private PenaltyInfo penaltyInfo = null;
	
	private String event;
	
	public PlayerInfo(String event, String name, String guid, Long id, String ip, Long level) {
		setEvent(event.toLowerCase());
		setName(name);
		setGuid(guid);
		setClientId(id);
		setIp(ip);
		setLevel(level);
	}
	
	/**
	 * @param addnote
	 * @param string
	 * @param string2
	 * @param l
	 * @param string3
	 * @param m
	 * @param notice
	 */
	public PlayerInfo(String event, String name, String guid, Long id, String ip, Long level, PenaltyInfo penalty) {
		this(event, name, guid, id, ip, level);
		this.setPenaltyInfo(penalty);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Date getUpdated() {
		return updated;
	}

	/**
	 * set updated date.
	 * do not allow dates in the future
	 * @param updated
	 */
	public void setUpdated(Date updated) {
		if (updated.after(new Date())) {
			this.updated = new Date();
		} else {
			this.updated = updated;	
		}
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	public PenaltyInfo getPenaltyInfo() {
		return this.penaltyInfo;
	}

	public PlayerInfo setPenaltyInfo(PenaltyInfo banInfo) {
		this.penaltyInfo = banInfo;
		return this;
	}

}
