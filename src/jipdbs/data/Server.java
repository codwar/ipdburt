package jipdbs.data;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

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
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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
