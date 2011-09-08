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

import iddb.core.model.Server;

import java.util.Date;

public class SearchResult {

	// A key to be able to retrieve player details.
	private Long key;

	// Latest known alias.
	private String name;

	// Latest
	private String ip;

	// Last time seen.
	private Date latest;

	// Server name.
	private Server server;

	// Playing right now.
	private boolean playing;

	private String banInfo;

	private String note;

	private long id;

	private String clientId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBanInfo() {
		return banInfo;
	}

	public void setBanInfo(String banInfo) {
		this.banInfo = banInfo;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Date getLatest() {
		return latest;
	}

	public void setLatest(Date latest) {
		this.latest = latest;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public String getIpSearch() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;

		return ip.substring(0, l + 1) + "*";
	}

	public String getIpZero() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;
		return ip.substring(0, l + 1) + "0";
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}