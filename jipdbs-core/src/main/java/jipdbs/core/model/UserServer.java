package jipdbs.core.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class UserServer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5791990892420934593L;
	
	private Key key;
	private Key server;
	private Key user;
	private Boolean owner;
	
	public UserServer(Entity entity) {
		this.key = entity.getKey();
		this.user = entity.getParent();
		this.server = (Key) entity.getProperty("server");
		this.owner = (Boolean) entity.getProperty("owner");
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("UserServer", this.user) : new Entity(this.key);
		entity.setProperty("server", this.server);
		entity.setUnindexedProperty("owner", this.owner);
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

	public Key getUser() {
		return user;
	}

	public void setUser(Key user) {
		this.user = user;
	}

	public Boolean getOwner() {
		return owner;
	}

	public void setOwner(Boolean owner) {
		this.owner = owner;
	}
	
}
