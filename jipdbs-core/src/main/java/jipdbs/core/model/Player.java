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
	
	public Player() {
		this.key = null;
	}

	public Player(Entity entity) {
		key = entity.getKey();
		server = entity.getParent();
		created = (Date) entity.getProperty("created");
		updated = (Date) entity.getProperty("updated");
		guid = (String) entity.getProperty("guid");
		banInfo = (String) entity.getProperty("baninfo");
		banInfoUpdated = (Date) entity.getProperty("baninfoupdated");
		try {
			level = (Long) entity.getProperty("level");			
		} catch (Exception e) {
			level = null;
		}
		try {
			clientId = (Long) entity.getProperty("clientId");
		} catch (Exception e) {
			clientId = null;
		}
		note = (String) entity.getProperty("note");
		connected = (Boolean) entity.getProperty("connected");
		if (connected == null) connected = false;
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("Player",
				this.getServer()) : new Entity(this.getKey());
		entity.setProperty("guid", guid);
		entity.setProperty("updated", updated);
		entity.setProperty("server", server);
		entity.setProperty("baninfoupdated", banInfoUpdated);
		entity.setProperty("clientId", clientId);
		entity.setProperty("connected", connected);
		entity.setUnindexedProperty("level", level);
		entity.setUnindexedProperty("note", note);
		entity.setUnindexedProperty("baninfo", banInfo);
		entity.setUnindexedProperty("created", created);
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
}
