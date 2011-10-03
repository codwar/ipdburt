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

import iddb.core.model.Alias;
import iddb.core.model.dao.AliasDAO;
import iddb.core.util.Functions;
import iddb.runtime.db.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliasDAOImpl implements AliasDAO {

	private static Logger logger = LoggerFactory.getLogger(AliasDAOImpl.class);
	
	public List<Alias> findByNickname(String query, int offset, int limit,
			int[] count) {
		String sqlCount = "select count(id) from alias where nickname = ? group by playerid";
		String sql = "select * from alias where nickname = ? group by playerid order by updated desc limit ?,?";
		List<Alias> list = new ArrayList<Alias>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setString(1, query);
			ResultSet rsC = stC.executeQuery();
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, query);
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Alias alias = new Alias();
				loadAlias(alias, rs);
				list.add(alias);
			}
		} catch (SQLException e) {
			logger.error("findByNickname", e);
		} catch (IOException e) {
			logger.error("findByNickname", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	public List<Alias> findBySimilar(String query, int offset, int limit,
			int[] count) {
		String sqlCount = "SELECT COUNT(id) FROM alias WHERE MATCH (nickname,normalized) AGAINST (? WITH QUERY EXPANSION) GROUP BY playerid";
		String sql = "SELECT * FROM alias WHERE MATCH (nickname,normalized) AGAINST (? WITH QUERY EXPANSION) GROUP BY playerid LIMIT ?,?";
		List<Alias> list = new ArrayList<Alias>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setString(1, query);
			ResultSet rsC = stC.executeQuery();
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, query);
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Alias alias = new Alias();
				loadAlias(alias, rs);
				list.add(alias);
			}
		} catch (SQLException e) {
			logger.error("findBySimilar", e);
		} catch (IOException e) {
			logger.error("findBySimilar", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	public List<Alias> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		String sqlCount = "select count(id) from alias where playerid = ?";
		String sql = "select * from alias where playerid = ? order by updated desc limit ?,?";
		List<Alias> list = new ArrayList<Alias>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setLong(1, player);
			ResultSet rsC = stC.executeQuery();
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Alias alias = new Alias();
				loadAlias(alias, rs);
				list.add(alias);
			}
		} catch (SQLException e) {
			logger.error("findByPlayer", e);
		} catch (IOException e) {
			logger.error("findByPlayer", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	private void loadAlias(Alias alias, ResultSet rs) throws SQLException {
		alias.setKey(rs.getLong("id"));
		alias.setPlayer(rs.getLong("playerid"));
		alias.setNickname(rs.getString("nickname"));
		alias.setCreated(rs.getTimestamp("created"));
		alias.setUpdated(rs.getTimestamp("updated"));
		alias.setCount(rs.getLong("count"));
	}
	
	public void save(Alias alias) {
		String sql;
		if (alias.getKey() == null) {
			sql = "insert into alias (playerid, nickname, created, updated, count, normalized) values (?, ?,?,?,?,?)"; 
		} else {
			sql = "update alias set playerid = ?," +
					"nickname = ?," +
					"created = ?," +
					"updated = ?," +
					"count = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setLong(1, alias.getPlayer());
			st.setString(2, alias.getNickname());
			if (alias.getCreated() == null) alias.setCreated(new Date());
			if (alias.getUpdated() == null) alias.setUpdated(new Date());			
			st.setTimestamp(3, new java.sql.Timestamp(alias.getCreated().getTime()));
			st.setTimestamp(4, new java.sql.Timestamp(alias.getUpdated().getTime()));
			st.setLong(5, alias.getCount());
			if (alias.getKey() != null) {
				st.setLong(6, alias.getKey());
			} else {
				st.setString(6, Functions.normalize(alias.getNickname()));
			}
			st.executeUpdate();
			if (alias.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					alias.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for alias {}", alias.getNickname());
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

	public void save(Collection<Alias> aliasses) {
		for (Alias alias : aliasses) {
			this.save(alias);
		}
	}

	public Alias findByPlayerAndNickname(Long player, String nickname) {
		String sql = "select * from alias where playerid = ? and nickname = ?";
		Alias alias = null;
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, player);
			st.setString(2, nickname);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				alias = new Alias();
				loadAlias(alias, rs);
			}
		} catch (SQLException e) {
			logger.error("findByPlayerAndNickname", e);
		} catch (IOException e) {
			logger.error("findByPlayerAndNickname", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return alias;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.AliasDAO#save(iddb.core.model.Alias, boolean)
	 */
	@Override
	public void save(Alias alias, boolean commit) {
		if (commit) {
			save(alias);
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.AliasDAO#save(java.util.Collection, boolean)
	 */
	@Override
	public void save(Collection<Alias> aliasses, boolean commit) {
		if (commit) {
			save(aliasses);
		}
	}

}
