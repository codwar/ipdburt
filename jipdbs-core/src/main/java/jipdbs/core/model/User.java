package jipdbs.core.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5587024051766779262L;

	private Key key;
	private Email email;

	public User(Entity entity) {
		key = entity.getKey();
		email = (Email) entity.getProperty("email");
	}

	public Entity toEntity() {
		Entity entity = key == null ? new Entity("User") : new Entity(key);
		entity.setProperty("email", email);
		return entity;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

}
