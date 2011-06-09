package jipdbs.core.model;

import java.io.Serializable;
import java.util.Date;

import jipdbs.core.util.Functions;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class AliasIP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4108078617319999336L;

	private Key key;
	private Key player;
	private String ip;
	private Date created;
	private Date updated;
	private Long count;
	
	public AliasIP(Key player) {
		this.player = player;
	}
	
	public AliasIP(Entity entity) {
		this.setKey(entity.getKey());
		this.setPlayer((Key) entity.getParent());
		this.setCreated((Date) entity.getProperty("created"));
		this.setUpdated((Date) entity.getProperty("updated"));
		this.setCount((Long) entity.getProperty("count"));
		this.setIp((String) Functions.decimalToIp((Long) entity.getProperty("ip")));
	}

	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("AliasIP",
					this.getPlayer()) : new Entity(this.getKey());
		entity.setProperty("updated", this.getUpdated());
		entity.setProperty("ip", Functions.ipToDecimal(this.getIp()));
		entity.setUnindexedProperty("created", this.getCreated());
		entity.setUnindexedProperty("count", this.getCount());
		return entity;
	}

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

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	
}
