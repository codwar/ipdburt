package jipdbs.info;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jipdbs.legacy.python.date.DateUtils;

public class PenaltyInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6801850365975055431L;

	private Date created;
	private String reason;
	private Long duration;
	private Long type;
	private String admin;
	
	public static String getDetail(String data) {
		if (data != null && data.startsWith("#")) {
				return new PenaltyInfo(data.substring(1)).toString();
		}
		return data;
	}
	
	public PenaltyInfo() {
	}
	
	public PenaltyInfo(String data) {
		this.parseRaw(data);
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setCreated(Long timestamp) {
		this.created = DateUtils.timestampToDate(timestamp);
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
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Date getExpires() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(this.created);
		calendar.add(Calendar.MINUTE, this.getDuration().intValue());
		return calendar.getTime();
	}
	
	@Override
	public String toString() {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("GMT-3"));
		DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		format2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
		String reason = this.getReason().equals("-") ? "No se indica motivo" : this.getReason();
		StringBuilder s = new StringBuilder("Baneado el ");
		s.append(format.format(this.getCreated())).append(" por ");
		s.append(reason);
		if (this.getDuration() > 0) {
			s.append(" hasta ");
			s.append(format2.format(this.getExpires()));
		}
		if (!this.getAdmin().equals("-")) {
			s.append(" (").append(getAdmin()).append(")");
		}
		return s.toString();
	}
	
	public void parseRaw(String data) {
		if (data.startsWith("#")) {
			data = data.substring(1);
		}
		String[] parts = data.split("::");
		setType(Long.parseLong(parts[0]));
		setCreated(Long.parseLong(parts[1]));
		setAdmin(parts[2]);
		if (parts[3].equals("-")) {
			setReason(null);
		} else {
			setReason(parts[3]);	
		}
		setDuration(Long.parseLong(parts[4]));
	}
	
	public String getRawData() {
		StringBuilder builder = new StringBuilder("#");
		builder.append(getType()).append("::");
		builder.append(DateUtils.dateToTimestamp(getCreated())).append("::");
		builder.append(getAdmin() == null ? "-" : getAdmin()).append("::");
		builder.append(getReason() == null ? "-" : getReason()).append("::");
		if (getDuration() == null) {
			builder.append("-1");	
		} else {
			builder.append(getDuration());
		}
		return builder.toString();
	}
	
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
}
