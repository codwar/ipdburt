package jipdbs.core.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class Penalty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1204044149724053986L;

	public static final Integer BAN = 0;
	public static final Integer NOTICE = 1;
	
	private Key key;
	private Player player;
	private Player admin;
	
	private Integer type;
	private Date created;
	private Date updated;
	private String reason;
	private Integer duration;
	private Boolean synced;
	private Boolean active;
	
	public Penalty(Player player) {
		this.player = player;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Boolean getSynced() {
		return synced;
	}

	public void setSynced(Boolean synced) {
		this.synced = synced;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getAdmin() {
		return admin;
	}

	public void setAdmin(Player admin) {
		this.admin = admin;
	}

}

