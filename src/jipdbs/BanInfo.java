package jipdbs;

public final class BanInfo {

	private String reason;
	private String guid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return String.format("<%s, %s>", guid, reason);
	}

}
