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
	private String hash = null;
	private String pbid;
	
	private String event;
	
	public PlayerInfo(String event) {
		setEvent(event.toLowerCase());
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
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Name: %s - PBID %s - GUID %s - ClientId %s - IP %s - Level %s - Updated %s - HashID %s - HasPenalty %s",
							this.name,
							this.pbid,
							this.guid,
							this.clientId,
							this.ip,
							this.level,
							this.updated.toString(),
							this.hash,
							Boolean.toString(this.penaltyInfo != null));
		 
	}

	public String getPbid() {
		return pbid;
	}

	public void setPbid(String pbid) {
		this.pbid = pbid;
	}
	
}
