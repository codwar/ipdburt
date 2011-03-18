package jipdbs.data;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class Alias {

	private Key key;

	private Key player;
	private String nickname;
	private String ip;
	private Date created;
	private Date updated;
	private int count;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getPlayer() {
		return player;
	}

	public void setPlayer(Key player) {
		this.player = player;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(nickname);
		b.append("[");
		b.append(ip);
		b.append("]");
		return b.toString();
	}
}
