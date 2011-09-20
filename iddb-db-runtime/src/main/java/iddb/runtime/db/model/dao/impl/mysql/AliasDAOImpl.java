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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliasDAOImpl implements AliasDAO {

	private static Logger logger = LoggerFactory.getLogger(AliasDAOImpl.class);
	
	public List<Alias> findByNickname(String query, int offset, int limit,
			int[] count) {
		String sqlCount = "SELECT COUNT(ID) FROM ALIAS WHERE NICKNAME = ? GROUP BY PLAYERID";
		String sql = "SELECT * FROM ALIAS WHERE NICKNAME = ? GROUP BY PLAYERID ORDER BY UPDATED DESC LIMIT ?,?";
		List<Alias> list = new ArrayList<Alias>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
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

	public List<Alias> findByNGrams(String query, int offset, int limit,
			int[] count) {
//		SELECT * FROM articles
//	    -> WHERE MATCH (title,body) AGAINST ('database');
		// TODO
//		String sqlCount = "SELECT COUNT(ID) FROM ALIAS WHERE PLAYERID = ?";
//		String sql = "SELECT * FROM ALIAS WHERE PLAYERID = ? ORDER BY UPDATED DESC LIMIT ?,?";
//
//		Collection<String> ngrams = NGrams.ngrams(query);
//
//		if (ngrams.size() == 0)
//			return Collections.emptyList();
//		
//		Connection conn = ConnectionFactory.getConnection();
//		List<Alias> list = new ArrayList<Alias>();
//		try {
//			PreparedStatement stC = conn.prepareStatement(sqlCount);
//			stC.setLong(1, player);
//			ResultSet rsC = stC.executeQuery(sqlCount);
//			if (rsC.next()) {
//				count[0] = rsC.getInt(1);
//			}
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setLong(1, player);
//			st.setInt(2, offset);
//			st.setInt(3, limit);
//			ResultSet rs = st.executeQuery();
//			while (rs.next()) {
//				Alias alias = new Alias();
//				loadAlias(alias, rs);
//				list.add(alias);
//			}
//		} catch (SQLException e) {
//			logger.error("findByPlayer", e);
//		} finally {
//			try {
//				conn.close();
//			} catch (Exception e) {
//			}
//		}
		return Collections.emptyList();
	}

	public List<Alias> findByPlayer(Long player, int offset, int limit,
			int[] count) {
		String sqlCount = "SELECT COUNT(ID) FROM ALIAS WHERE PLAYERID = ?";
		String sql = "SELECT * FROM ALIAS WHERE PLAYERID = ? ORDER BY UPDATED DESC LIMIT ?,?";
		List<Alias> list = new ArrayList<Alias>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
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
		alias.setKey(rs.getLong("ID"));
		alias.setPlayer(rs.getLong("PLAYERID"));
		alias.setNickname(rs.getString("NICKNAME"));
		alias.setCreated(rs.getTimestamp("CREATED"));
		alias.setUpdated(rs.getTimestamp("UPDATED"));
		alias.setCount(rs.getLong("COUNT"));
		Collection<String> ngrams = Arrays.asList(StringUtils.split(rs.getString("NGRAMS"), " "));
		alias.setNgrams(ngrams);
	}
	
	public void save(Alias alias) {
		String sql;
		if (alias.getKey() == null) {
			sql = "INSERT INTO ALIAS (PLAYERID, NICKNAME, NGRAMS, CREATED, UPDATED, COUNT) VALUES (?,?,?,?,?,?)"; 
		} else {
			sql = "UPDATE ALIAS SET PLAYERID = ?," +
					"NICKNAME = ?," +
					"NGRAMS = ?," +
					"CREATED = ?," +
					"UPDATED = ?," +
					"COUNT = ? WHERE ID = ? LIMIT 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setLong(1, alias.getPlayer());
			st.setString(2, alias.getNickname());
			st.setString(3, Functions.join(alias.getNgrams(), " "));
			st.setTimestamp(4, new java.sql.Timestamp(alias.getCreated().getTime()));
			st.setTimestamp(5, new java.sql.Timestamp(alias.getUpdated().getTime()));
			st.setLong(6, alias.getCount());
			if (alias.getKey() != null) st.setLong(7, alias.getKey());
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
		String sql = "SELECT * FROM ALIAS WHERE PLAYERID = ? AND NICKNAME = ?";
		Alias alias = null;
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
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
