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
package iddb.core.model;

import java.io.Serializable;
import java.util.Date;

public class PenaltyHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5092229218912990043L;

	public static final Integer ST_PENDING = 0;
	public static final Integer ST_WAITING = 1;
	public static final Integer ST_DONE = 2;
	public static final Integer ST_ERROR = 3;
	public static final Integer ST_CANCEL = 4;
	
	private Long key;
	private Long adminId;
	private Long penaltyId;
	private Date created;
	private Date updated;
	private Integer status;
	private String error;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public Long getPenaltyId() {
		return penaltyId;
	}
	public void setPenaltyId(Long penaltyId) {
		this.penaltyId = penaltyId;
	}
	
}
