package jipdbs.core.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Player implements Serializable {

	private static final long serialVersionUID = 6984528322229982497L;

	private Key key;
	private Key server;
	private String guid;
	private Date created;
	private Date updated;
	private Date banInfoUpdated;
	private String banInfo;
	private Long clientId;
	private Long level;
	private String note;
	private Boolean connected;
	private String nickname;
	private String ip;
	
	public Player() {
		this.key = null;
	}

	public Player(Entity entity) {
		this.key = entity.getKey();
		this.server = entity.getParent();
		this.created = (Date) entity.getProperty("created");
		this.updated = (Date) entity.getProperty("updated");
		this.guid = (String) entity.getProperty("guid");
		this.banInfo = (String) entity.getProperty("baninfo");
		this.banInfoUpdated = (Date) entity.getProperty("baninfoupdated");
		try {
			this.level = (Long) entity.getProperty("level");			
		} catch (Exception e) {
			this.level = null;
		}
		try {
			this.clientId = (Long) entity.getProperty("clientId");
		} catch (Exception e) {
			this.clientId = null;
		}
		this.note = (String) entity.getProperty("note");
		this.connected = (Boolean) entity.getProperty("connected");
		if (this.connected == null) this.connected = false;
		this.nickname = (String) entity.getProperty("nickname");
		this.ip = (String) entity.getProperty("ip");
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("Player",
				this.getServer()) : new Entity(this.getKey());
		entity.setProperty("guid", this.guid);
		entity.setProperty("updated", this.updated);
		entity.setProperty("server", this.server);
		entity.setProperty("baninfoupdated", this.banInfoUpdated);
		entity.setProperty("clientId", this.clientId);
		entity.setProperty("connected", this.connected);
		entity.setUnindexedProperty("level", this.level);
		entity.setUnindexedProperty("note", this.note);
		entity.setUnindexedProperty("baninfo", this.banInfo);
		entity.setUnindexedProperty("created", this.created);
		entity.setUnindexedProperty("nickname", this.nickname);
		entity.setUnindexedProperty("ip", this.ip);
		return entity;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getServer() {
		return server;
	}

	public void setServer(Key server) {
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

	public String getBanInfo() {
		return banInfo;
	}

	public void setBanInfo(String banInfo) {
		this.banInfo = banInfo;
	}

	public Date getBanInfoUpdated() {
		return banInfoUpdated;
	}

	public void setBanInfoUpdated(Date banInfoUpdated) {
		this.banInfoUpdated = banInfoUpdated;
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
