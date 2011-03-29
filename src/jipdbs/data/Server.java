package jipdbs.data;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Server implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1356474862304251338L;

	private Key key;
	private String uid;
	private String name;
	private Email admin;
	private Date created;
	private Date updated;
	private int onlinePlayers;
	private String address;
	private String keyString;

	public Server() {
	}

	public Server(Entity entity) {
		this.setKey(entity.getKey());
		this.setCreated((Date) entity.getProperty("created"));
		this.setUpdated((Date) entity.getProperty("updated"));
		this.setAdmin((Email) entity.getProperty("admin"));
		this.setName((String) entity.getProperty("name"));
		this.setUid((String) entity.getProperty("uid"));
		this
				.setOnlinePlayers(((Long) entity.getProperty("players"))
						.intValue());
		this.setAddress((String) entity.getProperty("ip"));
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("Server")
				: new Entity(this.getKey());

		entity.setProperty("name", this.getName());
		entity.setProperty("created", this.getCreated());
		entity.setProperty("updated", this.getUpdated());
		entity.setProperty("admin", this.getAdmin());
		entity.setProperty("uid", this.getUid());
		entity.setProperty("players", this.getOnlinePlayers());
		entity.setProperty("ip", this.getAddress());
		return entity;
	}

	public Key getKey() {
		return key;
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public void setKey(Key key) {
		this.key = key;
		this.keyString = KeyFactory.keyToString(key);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Email getAdmin() {
		return admin;
	}

	public void setAdmin(Email admin) {
		this.admin = admin;
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

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("\"");
		b.append(name);
		b.append("[");
		b.append(uid);
		b.append("]");
		return b.toString();
	}
}
