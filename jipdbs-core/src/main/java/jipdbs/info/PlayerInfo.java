package jipdbs.info;

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
	private PenaltyInfo penaltyInfo = null;
	
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
		this.name = name.trim();
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

	/**
	 * set updated date.
	 * do not allow dates in the future
	 * @param updated
	 */
	public void setUpdated(Date updated) {
		if (updated.after(new Date())) {
			this.updated = new Date();
		} else {
			this.updated = updated;	
		}
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	public PenaltyInfo getPenaltyInfo() {
		return this.penaltyInfo;
	}

	public void setPenaltyInfo(PenaltyInfo banInfo) {
		this.penaltyInfo = banInfo;
	}

}
