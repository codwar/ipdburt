package jipdbs.info;

import java.util.Date;

import jipdbs.core.model.Server;

public class SearchResult {

	// A key to be able to retrieve player details.
	private String key;

	// Latest known alias.
	private String name;

	// Latest
	private String ip;

	// Last time seen.
	private Date latest;

	// Server name.
	private Server server;

	// Playing right now.
	private boolean playing;

	private String banInfo;

	private long id;
	
	private String clientId;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBanInfo() {
		return banInfo;
	}

	public void setBanInfo(String banInfo) {
		this.banInfo = banInfo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

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

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Date getLatest() {
		return latest;
	}

	public void setLatest(Date latest) {
		this.latest = latest;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public String getIpSearch() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;

		return ip.substring(0, l + 1) + "*";
	}
	
	public String getIpZero() {
		if (ip == null)
			return null;
		int l = ip.lastIndexOf('.');
		if (l < 0)
			return null;
		return ip.substring(0, l + 1) + "0";		
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}