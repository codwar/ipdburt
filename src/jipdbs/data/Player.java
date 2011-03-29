package jipdbs.data;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984528322229982497L;

	private Key key;
	private Key server;
	private String guid;
	private Date created;
	private Date updated;
	private String banInfo;

	public Player() {
	}

	public Player(Entity entity) {
		this.setKey(entity.getKey());
		this.setServer((Key) entity.getParent());
		this.setCreated((Date) entity.getProperty("created"));
		this.setUpdated((Date) entity.getProperty("updated"));
		this.setGuid((String) entity.getProperty("guid"));
		this.setBanInfo((String) entity.getProperty("baninfo"));
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("Player", this.getServer())
				: new Entity(this.getKey());
		entity.setProperty("baninfo", this.getBanInfo());
		entity.setProperty("created", this.getCreated());
		entity.setProperty("guid", this.getGuid());
		entity.setProperty("updated", this.getUpdated());
		entity.setProperty("server", this.getServer());
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

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(server);
		b.append("[");
		b.append(guid);
		b.append("]");
		return b.toString();
	}
}
