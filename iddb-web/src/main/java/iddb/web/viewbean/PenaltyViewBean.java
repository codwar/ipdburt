/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.web.viewbean;

import iddb.core.model.Penalty;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;

public class PenaltyViewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113493728931503717L;

	private Long key;
	private Date created;
	private String reason;
	private Long duration;
	private String admin;
	private boolean blank;
	
	public PenaltyViewBean() {
		this.blank = false;
	}
	public PenaltyViewBean(boolean blank) {
		this.blank = blank;
	}
	
	public Integer getType() {
		return Penalty.BAN;
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
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Date getExpires() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(this.created);
		calendar.add(Calendar.MINUTE, this.getDuration().intValue());
		return calendar.getTime();
	}	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	@Override
	public String toString() {
		if (this.blank) return MessageResource.getMessage("banned");
		
		StringBuilder s = new StringBuilder();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("GMT-3"));
		
		s.append("Baneado el ").append(format.format(this.getCreated()));
		if (this.getDuration() > 0) {
			DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			format2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
			s.append(" hasta ").append(format2.format(this.getExpires()));
		}
		if (StringUtils.isNotEmpty(this.getReason())) {
			s.append(". Motivo: ").append(this.getReason());
		} else {
			s.append(". No indica motivo.");
		}
		if (StringUtils.isNotEmpty(this.getAdmin())) {
			s.append(" (").append(getAdmin()).append(")");
		}
		return s.toString();
	}	
}
