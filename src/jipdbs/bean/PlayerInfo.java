package jipdbs.bean;

import java.io.Serializable;
import java.util.Date;

public class PlayerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8609646730753062685L;

	private String name;
	private String guid;
	private Long clientId;
	private String ip;
	private Long level;
	private Date updated = new Date();
	private String extra = null;
	
	private String event;
	
	public PlayerInfo(String event, String name, String guid, Long id, String ip, Long level) {
		setEvent(event.toLowerCase());
		setName(name);
		setGuid(guid);
		setClientId(id);
		setIp(ip);
		setLevel(level);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	public BanInfo getBanInfo() {
		return new BanInfo(this.extra);
	}

}
