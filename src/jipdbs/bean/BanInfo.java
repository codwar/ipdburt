package jipdbs.bean;

import java.io.Serializable;
import java.util.Date;

import org.datanucleus.util.StringUtils;

public class BanInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6801850365975055431L;

	private Date created;
	private String reason;
	private Long duration;
	private String type;
	
	public BanInfo() {
	}
	
	public BanInfo(String data) {
		String[] parts = StringUtils.split(data, "::");
		type = parts[0];
		created = new Date(Long.parseLong(parts[1]) * 1000L);
		duration = Long.parseLong(parts[2]);
		reason = parts[3];
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
