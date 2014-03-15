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

import iddb.core.model.Server;
import iddb.info.AliasResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


public class PlayerViewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 21345435389314447L;

	private String key;
	private String name;
	private Server server;
	private String ip;
	private Date updated;
	private Collection<AliasResult> aliases;
	private String level;
	private String clientId;
	private boolean playing;
	private String note;
	private String guid;
	private PenaltyViewBean banInfo;
	private String pbid;
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public PenaltyViewBean getBanInfo() {
		return banInfo;
	}
	public void setBanInfo(PenaltyViewBean banInfo) {
		this.banInfo = banInfo;
	}
	public Collection<AliasResult> getAliases() {
		return aliases;
	}
	public void setAliases(Collection<AliasResult> aliases) {
		this.aliases = aliases;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIpZero() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;
		return ip.substring(0, l + 1) + "0";		
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPbid() {
		return pbid;
	}
	public void setPbid(String pbid) {
		this.pbid = pbid;
	}	
	
}
