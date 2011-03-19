package jipdbs;


public class SearchResult {

	// A key to be able to retrieve player details.
	private String key;

	// Latest known alias.
	private String name;

	// Latest
	private String ip;

	// Last time seen. A timestamp or "Playing".
	private String latest;

	// Server name.
	private String server;

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

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}