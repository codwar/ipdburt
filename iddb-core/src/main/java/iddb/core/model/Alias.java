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

public class Alias implements Serializable {

	private static final long serialVersionUID = 3801797021472636813L;

	private Long key;
	private Long player;
	private Long server;

	private String nickname;
	private Date created;
	private Date updated;
	private Long count;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPlayer() {
		return player;
	}

	public void setPlayer(Long player) {
		this.player = player;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getServer() {
		return server;
	}

	public void setServer(Long server) {
		this.server = server;
	}

	@Override
	public int hashCode() {
		return player.hashCode() ^ nickname.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Alias))
			return false;

		if (obj == this)
			return true;

		Alias other = (Alias) obj;

		return player.equals(other.getPlayer())
				&& nickname.equals(other.getNickname());
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(nickname);
		return b.toString();
	}
}
