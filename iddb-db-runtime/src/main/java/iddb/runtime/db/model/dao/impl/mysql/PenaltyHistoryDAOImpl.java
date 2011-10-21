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
package iddb.runtime.db.model.dao.impl.mysql;

import iddb.core.model.PenaltyHistory;
import iddb.core.model.dao.PenaltyHistoryDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.runtime.db.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyHistoryDAOImpl implements PenaltyHistoryDAO {

	private static Logger logger = LoggerFactory.getLogger(PenaltyHistoryDAOImpl.class);
	
	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#save(iddb.core.model.PenaltyHistory)
	 */
	@Override
	public void save(PenaltyHistory penalty) {
		String sql;
		if (penalty.getKey() == null) {
			sql = "insert into penalty_history (status, updated, error, penaltyid, adminid, created) values (?,?,?,?,?,?)"; 
		} else {
			sql = "update penalty_history set status = ?," +
					"updated = ?," +
					"error = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (penalty.getUpdated() == null) penalty.setUpdated(new Date());
			if (penalty.getCreated() == null) penalty.setCreated(new Date());
			st.setInt(1, penalty.getStatus());
			st.setTimestamp(2, new java.sql.Timestamp(penalty.getUpdated().getTime()));
			if (penalty.getError() == null || "".equals(penalty.getError())) {
				st.setNull(3, Types.VARCHAR);
			} else {
				st.setString(3, penalty.getError());
			}
			if (penalty.getKey() != null) st.setLong(4, penalty.getKey());
			else {
				st.setLong(4, penalty.getPenaltyId());
				st.setLong(5, penalty.getAdminId());
				st.setTimestamp(6, new java.sql.Timestamp(penalty.getCreated().getTime()));	
			}
			st.executeUpdate();
			if (penalty.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					penalty.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for penalty history {}", penalty.getPenaltyId());
				}
			}
		} catch (SQLException e) {
			logger.error("Save", e);
		} catch (IOException e) {
			logger.error("Save", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#get(java.lang.Long)
	 */
	@Override
	public PenaltyHistory get(Long id) throws EntityDoesNotExistsException {
		String sql = "select * from penalty_history where id = ?";
		Connection conn = null;
		PenaltyHistory penalty = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				penalty = new PenaltyHistory();
				loadPenaltyHistory(penalty, rs);
			} else {
				throw new EntityDoesNotExistsException("Penalty history with id %s was not found", id.toString());
			}
		} catch (SQLException e) {
			logger.error("get", e);
		} catch (IOException e) {
			logger.error("get", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return penalty;
	}

	/**
	 * @param penalty
	 * @param rs
	 * @throws SQLException 
	 */
	private void loadPenaltyHistory(PenaltyHistory penalty, ResultSet rs) throws SQLException {
		penalty.setKey(rs.getLong("id"));
		penalty.setAdminId(rs.getLong("adminid"));
		penalty.setPenaltyId(rs.getLong("penaltyid"));
		penalty.setError(rs.getString("error"));
		penalty.setStatus(rs.getInt("status"));
		penalty.setCreated(rs.getDate("created"));
		penalty.setUpdated(rs.getDate("updated"));
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#listByPenaltyId(java.lang.Long)
	 */
	@Override
	public List<PenaltyHistory> listByPenaltyId(Long id) {
		String sql = "select * from penalty_history where penaltyid = ?";
		List<PenaltyHistory> list = new ArrayList<PenaltyHistory>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				PenaltyHistory penalty = new PenaltyHistory();
				loadPenaltyHistory(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("listByPenaltyId", e);
		} catch (IOException e) {
			logger.error("listByPenaltyId", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#getLastByPenalty(java.lang.Long)
	 */
	@Override
	public PenaltyHistory getLastByPenalty(Long id)
			throws EntityDoesNotExistsException {
		String sql = "select * from penalty_history where penaltyid = ? order by created desc limit 1";
		Connection conn = null;
		PenaltyHistory penalty = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				penalty = new PenaltyHistory();
				loadPenaltyHistory(penalty, rs);
			} else {
				throw new EntityDoesNotExistsException("Penalty history for penalty %s was not found", id.toString());
			}
		} catch (SQLException e) {
			logger.error("getLastByPenalty", e);
		} catch (IOException e) {
			logger.error("getLastByPenalty", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return penalty;
	}

}
