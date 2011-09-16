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
import java.sql.Date;
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
			sql = "INSERT INTO SERVER (UID, NAME, ADMIN, CREATED, UPDATED, ONLINEPLAYERS, ADDRESS, PLUGINVERSION, MAXLEVEL, DIRTY, PERMISSION) VALUES (?,?,?,?,?,?,?,?,?,?,?)"; 
		} else {
			sql = "UPDATE SERVER SET UID = ?, NAME = ?, ADMIN = ?, CREATED = ?, UPDATED = ?, ONLINEPLAYERS = ?, ADDRESS = ?, PLUGINVERSION = ?, MAXLEVEL = ?, DIRTY = ?, PERMISSION = ? WHERE ID = ?";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, server.getUid());
			st.setString(2, server.getName());
			st.setString(3, server.getAdminEmail());
			st.setDate(4, new Date(server.getCreated().getTime()));
			st.setDate(5, new Date(server.getUpdated().getTime()));
			st.setInt(6, server.getOnlinePlayers());
			st.setString(7, server.getAddress());
			st.setString(8, server.getPluginVersion());
			st.setLong(9, server.getMaxLevel());
			st.setBoolean(10, server.getDirty());
			st.setInt(11, server.getPermission());
			if (server.getKey() != null) st.setLong(12, server.getKey());
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

	@Override
	public List<Server> findAll(int offset, int limit, int[] count) {
		String sqlCount = "SELECT COUNT(ID) FROM SERVER";
		String sql = "SELECT * FROM SERVER LIMIT ?,?";
		Connection conn = null;
		List<Server> list = new ArrayList<Server>();
		try {
			conn = ConnectionFactory.getConnection();
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
		String sql = "SELECT * FROM SERVER WHERE UID = ?";
		Connection conn = null;
		Server server = null;
		try {
			conn = ConnectionFactory.getConnection();
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
		server.setKey(rs.getLong("ID"));
		server.setUid(rs.getString("UID"));
		server.setName(rs.getString("NAME"));
		server.setAdminEmail(rs.getString("ADMIN"));
		server.setCreated(rs.getDate("CREATED"));
		server.setUpdated(rs.getDate("UPDATED"));
		server.setOnlinePlayers(rs.getInt("ONLINEPLAYERS"));
		server.setAddress(rs.getString("ADDRESS"));
		server.setPluginVersion(rs.getString("PLUGINVERSION"));
		server.setMaxLevel(rs.getLong("MAXLEVEL"));
		server.setDirty(rs.getBoolean("DIRTY"));
		server.setPermission(rs.getInt("PERMISSION"));
	}

	@Override
	public Server get(Long key) throws EntityDoesNotExistsException {
		String sql = "SELECT * FROM SERVER WHERE ID = ?";
		Connection conn = null;
		Server server = null;
		try {
			conn = ConnectionFactory.getConnection();
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
		String sql = "SELECT * FROM SERVER WHERE UPDATED <= ?";
		Connection conn = null;
		List<Server> list = new ArrayList<Server>();
		try {
			conn = ConnectionFactory.getConnection();
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

}
