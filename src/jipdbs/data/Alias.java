package jipdbs.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import jipdbs.util.Functions;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Alias implements Serializable {

	private static final long serialVersionUID = 3801797021472636813L;

	private Key key;

	private Key player;
	private String nickname;
	private Collection<String> ngrams;
	private String ip;
	private Date created;
	private Date updated;
	private int count;

	public Alias() {
	}
	
	@SuppressWarnings("unchecked")
	public Alias(Entity entity) {
		this.setKey(entity.getKey());
		this.setPlayer((Key) entity.getParent());
		this.setCreated((Date) entity.getProperty("created"));
		this.setUpdated((Date) entity.getProperty("updated"));
		this.setCount(((Long) entity.getProperty("count")).intValue());
		this.setIp((String) Functions.decimalToIp((Long) entity.getProperty("ip")));
		this.setNickname((String) entity.getProperty("nickname"));
		this.setNgrams((Collection<String>) entity.getProperty("ngrams"));
	}
	
	public Entity toEntity() {
		Entity entity = this.getKey() == null ? new Entity("Alias", this.getPlayer()) : new Entity(this.getKey());
		entity.setProperty("created", this.getCreated());
		entity.setProperty("updated", this.getUpdated());
		entity.setProperty("count", this.getCount());
		entity.setProperty("ip", Functions.ipToDecimal(this.getIp()));
		entity.setProperty("nickname", this.getNickname());
		entity.setProperty("ngrams", this.getNgrams());
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
	
	public String getMaskedIp() {
		return Functions.maskIpAddress(this.ip);
	}

	public Collection<String> getNgrams() {
		return ngrams;
	}

	public void setNgrams(Collection<String> ngrams) {
		this.ngrams = ngrams;
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
