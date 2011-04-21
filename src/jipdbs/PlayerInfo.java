package jipdbs;

import java.util.Date;

public final class PlayerInfo {

	private String name;
	private String ip;
	private String guid;
	private String id;
	private Date updated;
	private String level;
	private String baninfo;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return String.format("<%s, %s, %s>", name, ip, guid);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBaninfo() {
		return baninfo;
	}

	public void setBaninfo(String baninfo) {
		this.baninfo = baninfo;
	}

}