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
package iddb.runtime.j2ee.model.dao.impl;

import iddb.core.model.Penalty;
import iddb.core.model.dao.PenaltyDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.runtime.j2ee.db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyDAOImpl implements PenaltyDAO {

	private static Logger logger = LoggerFactory.getLogger(PenaltyDAOImpl.class);
	
	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#save(jipdbs.model.entity.Penalty)
	 */
	@Override
	public void save(Penalty penalty) {
		String sql;
		if (penalty.getKey() == null) {
			sql = "INSERT INTO PENALTY (PLAYERID, ADMINID, TYPE, REASON, DURATION, SYNCED, ACTIVE, CREATED, UPDATED) VALUES (?,?,?,?,?,?,?,?,?)"; 
		} else {
			sql = "UPDATE PENALTY SET PLAYERID = ?," +
					"ADMINID = ?," +
					"TYPE = ?," +
					"REASON = ?," +
					"DURATION = ?," +
					"SYNCED = ?," +
					"ACTIVE = ?," +
					"CREATED = ?," +
					"UPDATED = ? WHERE ID = ?";
		}
		Connection conn = ConnectionFactory.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, penalty.getPlayer());
			st.setLong(2, penalty.getAdmin());
			st.setInt(3, penalty.getType().intValue());
			st.setString(4, penalty.getReason());
			st.setLong(5, penalty.getDuration());
			st.setBoolean(6, penalty.getSynced());
			st.setBoolean(7, penalty.getActive());
			st.setDate(8, new java.sql.Date(penalty.getCreated().getTime()));
			st.setDate(9, new java.sql.Date(penalty.getUpdated().getTime()));
			if (penalty.getKey() != null) st.setLong(10, penalty.getKey());
			st.executeUpdate();
			if (penalty.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					penalty.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for penalty player id {}", penalty.getPlayer());
				}
			}
		} catch (SQLException e) {
			logger.error("Save", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#delete(jipdbs.model.entity.Penalty)
	 */
	@Override
	public void delete(Penalty penalty) {
		String sql = "DELETE FROM PENALTY WHERE ID = ?";
		Connection conn = ConnectionFactory.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, penalty.getKey());
			int r = st.executeUpdate();
			logger.debug("{} penalty removed", r);
		} catch (SQLException e) {
			logger.error("get", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#get(java.lang.Long)
	 */
	@Override
	public Penalty get(Long key) throws EntityDoesNotExistsException {
		String sql = "SELECT * FROM PENALTY WHERE ID = ?";
		Connection conn = ConnectionFactory.getConnection();
		Penalty penalty = null;
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				penalty = new Penalty();
				loadPenalty(penalty, rs);
			} else {
				throw new EntityDoesNotExistsException("Penalty with id %s was not found", key.toString());
			}
		} catch (SQLException e) {
			logger.error("get", e);
		} finally {
			try {
				conn.close();
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
	private void loadPenalty(Penalty penalty, ResultSet rs) throws SQLException {
		penalty.setKey(rs.getLong("ID"));
		penalty.setPlayer(rs.getLong("PLAYERID"));
		penalty.setAdmin(rs.getLong("ADMINID"));
		penalty.setType(new Long((rs.getInt("TYPE"))));
		penalty.setReason(rs.getString("REASON"));
		penalty.setDuration(rs.getLong("DURATION"));
		penalty.setSynced(rs.getBoolean("SYNCED"));
		penalty.setActive(rs.getBoolean("ACTIVE"));
		penalty.setCreated(rs.getDate("CREATED"));
		penalty.setUpdated(rs.getDate("UPDATED"));
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByPlayer(java.lang.Long)
	 */
	@Override
	public List<Penalty> findByPlayer(Long player) {
		String sql = "SELECT * FROM PENALTY WHERE PLAYERID = ?";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByPlayer", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByPlayer(java.lang.Long, int)
	 */
	@Override
	public List<Penalty> findByPlayer(Long player, int limit) {
		String sql = "SELECT * FROM PENALTY WHERE PLAYERID = ? LIMIT ?";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setInt(2, limit);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByPlayer", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByType(java.lang.Long, int, int, int[])
	 */
	@Override
	public List<Penalty> findByType(Long type, int offset, int limit,
			int[] count) {
		String sqlCount = "SELECT COUNT(ID) FROM PENALTY WHERE TYPE = ?";
		String sql = "SELECT * FROM PENALTY WHERE TYPE = ? ORDER BY CREATED DESC LIMIT ?,?";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setInt(1, type.intValue());
			ResultSet rsC = stC.executeQuery(sqlCount);
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			stC.setInt(1, type.intValue());
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByType", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByPlayerAndTypeAndActive(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Penalty> findByPlayerAndTypeAndActive(Long player, Long type) {
		String sql = "SELECT * FROM PENALTY WHERE PLAYERID = ? AND TYPE = ? AND ACTIVE = ? ORDER BY CREATED DESC";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setInt(2, type.intValue());
			st.setBoolean(3, true);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByPlayerAndTypeAndActive", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByPlayerAndType(java.lang.Long, java.lang.Long, int, int, int[])
	 */
	@Override
	public List<Penalty> findByPlayerAndType(Long player, Long type, int offset, int limit, int[] count) {
		String sqlCount = "SELECT COUNT(ID) FROM PENALTY WHERE PLAYERID = ? AND TYPE = ?";
		String sql = "SELECT * FROM PENALTY WHERE PLAYERID = ? AND TYPE = ? ORDER BY CREATED DESC LIMIT ?,?";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setLong(1, player);
			stC.setInt(2, type.intValue());
			ResultSet rsC = stC.executeQuery(sqlCount);
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setInt(2, type.intValue());
			st.setInt(3, offset);
			st.setInt(4, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByPlayerAndType", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#save(java.util.List)
	 */
	@Override
	public void save(List<Penalty> list) {
		for (Penalty p : list) {
			this.save(p);
		}
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#delete(java.util.List)
	 */
	@Override
	public void delete(List<Penalty> list) {
		String sql = "DELETE FROM PENALTY WHERE ID = ?";
		Connection conn = ConnectionFactory.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			for (Penalty p : list) {
				st.setLong(1, p.getKey());
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			logger.error("delete", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see jipdbs.dao.PenaltyDAO#findByPlayerAndType(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Penalty> findByPlayerAndType(Long player, Long type) {
		String sql = "SELECT * FROM PENALTY WHERE PLAYERID = ? AND TYPE = ? ORDER BY CREATED DESC";
		Connection conn = ConnectionFactory.getConnection();
		List<Penalty> list = new ArrayList<Penalty>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setInt(2, type.intValue());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Penalty penalty = new Penalty();
				loadPenalty(penalty, rs);
				list.add(penalty);
			}
		} catch (SQLException e) {
			logger.error("findByPlayerAndType", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}


}
