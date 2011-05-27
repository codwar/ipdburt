package jipdbs.info;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import jipdbs.core.data.Server;

public class PlayerInfoView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	private Server server;
	private String ip;
	private Date updated;
	private String banInfo;
	private Collection<AliasResult> aliases;
	private String level;
	private String clientId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getBanInfo() {
		return banInfo;
	}
	public void setBanInfo(String banInfo) {
		this.banInfo = banInfo;
	}
	public Collection<AliasResult> getAliases() {
		return aliases;
	}
	public void setAliases(Collection<AliasResult> aliases) {
		this.aliases = aliases;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIpZero() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;
		return ip.substring(0, l + 1) + "0";		
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}	
	
}
