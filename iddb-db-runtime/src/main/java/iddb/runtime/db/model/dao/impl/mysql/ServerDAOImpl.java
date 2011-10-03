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

import iddb.core.model.Server;
import iddb.core.model.dao.ServerDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.runtime.db.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDAOImpl implements ServerDAO {

	private static Logger logger = LoggerFactory.getLogger(ServerDAOImpl.class);
	
	@Override
	public void save(Server server) {
		String sql;
		if (server.getKey() == null) {
			sql = "insert into server (uid, name, admin, created, updated, onlineplayers, address, pluginversion, maxlevel, isdirty, permission, disabled) values (?,?,?,?,?,?,?,?,?,?,?)"; 
		} else {
			sql = "update server set uid = ?, name = ?, admin = ?, created = ?, updated = ?, onlineplayers = ?, address = ?, pluginversion = ?, maxlevel = ?, isdirty = ?, permission = ?, disabled = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, server.getUid());
			st.setString(2, server.getName());
			st.setString(3, server.getAdminEmail());
			if (server.getCreated() == null) server.setCreated(new java.util.Date());
			if (server.getUpdated() == null) server.setUpdated(new java.util.Date());
			st.setTimestamp(4, new Timestamp(server.getCreated().getTime()));
			st.setTimestamp(5, new Timestamp(server.getUpdated().getTime()));
			st.setInt(6, server.getOnlinePlayers());
			st.setString(7, server.getAddress());
			st.setString(8, server.getPluginVersion());
			st.setLong(9, server.getMaxLevel());
			st.setBoolean(10, server.getDirty());
			st.setInt(11, server.getPermission());
			st.setBoolean(12, server.getDisabled());
			if (server.getKey() != null) st.setLong(13, server.getKey());
			st.executeUpdate();
			if (server.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					server.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for server {}", server.getUid());
				}
			}
		} catch (SQLException e) {
			logger.error("Save", e.getMessage());
		} catch (IOException e) {
			logger.error("Save", e.getMessage());
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {
		String sqlCount = "select count(id) from server";
		String sql = "select * from server order by updated desc limit ?,?";
		Connection conn = null;
		List<Server> list = new ArrayList<Server>();
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			Statement stC = conn.createStatement();
			ResultSet rsC = stC.executeQuery(sqlCount);
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, offset);
			st.setInt(2, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Server server = new Server();
				loadServer(server, rs);
				list.add(server);
			}
		} catch (SQLException e) {
			logger.error("findAll", e);
		} catch (IOException e) {
			logger.error("findAll", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	public Server findByUid(String uid) {
		String sql = "select * from server where uid = ?";
		Connection conn = null;
		Server server = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, uid);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				server = new Server();
				loadServer(server, rs);
			}
		} catch (SQLException e) {
			logger.error("findByUid", e);
		} catch (IOException e) {
			logger.error("findByUid", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return server;
	}

	private void loadServer(Server server, ResultSet rs) throws SQLException {
		server.setKey(rs.getLong("id"));
		server.setUid(rs.getString("uid"));
		server.setName(rs.getString("name"));
		server.setAdminEmail(rs.getString("admin"));
		server.setCreated(rs.getTimestamp("created"));
		server.setUpdated(rs.getTimestamp("updated"));
		server.setOnlinePlayers(rs.getInt("onlineplayers"));
		server.setAddress(rs.getString("address"));
		server.setPluginVersion(rs.getString("pluginversion"));
		server.setMaxLevel(rs.getLong("maxlevel"));
		server.setDirty(rs.getBoolean("isdirty"));
		server.setPermission(rs.getInt("permission"));
		server.setDisabled(rs.getBoolean("disabled"));
	}

	@Override
	public Server get(Long key) throws EntityDoesNotExistsException {
		String sql = "select * from server where id = ?";
		Connection conn = null;
		Server server = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				server = new Server();
				loadServer(server, rs);
			} else {
				throw new EntityDoesNotExistsException("Server with id %s was not found", key.toString());
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
		return server;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.ServerDAO#listNotUpdatedSince(java.util.Date)
	 */
	@Override
	public List<Server> listNotUpdatedSince(java.util.Date date) {
		String sql = "select * from server where updated <= ?";
		Connection conn = null;
		List<Server> list = new ArrayList<Server>();
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setTimestamp(1, new Timestamp(date.getTime()));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Server server = new Server();
				loadServer(server, rs);
				list.add(server);
			}
		} catch (SQLException e) {
			logger.error("listNotUpdatedSince", e);
		} catch (IOException e) {
			logger.error("listNotUpdatedSince", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	public List<Server> findEnabled(int offset, int limit, int[] count) {
		String sqlCount = "select count(id) from server where disabled = 0";
		String sql = "select * from server where disabled = 0 order by updated desc limit ?,?";
		Connection conn = null;
		List<Server> list = new ArrayList<Server>();
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			Statement stC = conn.createStatement();
			ResultSet rsC = stC.executeQuery(sqlCount);
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, offset);
			st.setInt(2, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Server server = new Server();
				loadServer(server, rs);
				list.add(server);
			}
		} catch (SQLException e) {
			logger.error("findEnabled", e);
		} catch (IOException e) {
			logger.error("findEnabled", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}
	
}
