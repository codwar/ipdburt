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

import iddb.core.model.Player;
import iddb.core.model.dao.PlayerDAO;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDAOImpl implements PlayerDAO {

	private static Logger logger = LoggerFactory.getLogger(PlayerDAOImpl.class);
	
	@Override
	public Player findByServerAndHash(Long server, String guid) {
		String sql = "select * from player where serverid = ? and guid = ?";
		Connection conn = null;
		Player player = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, server);
			st.setString(2, guid);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				player = new Player();
				loadPlayer(player, rs);
			}
		} catch (SQLException e) {
			logger.error("findByServerAndHash: {}", e);
		} catch (IOException e) {
			logger.error("findByServerAndHash: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return player;
	}

	@Override
	/**
	 * 
	 */
	public List<Player> findByServer(Long key, int offset, int limit,int[] count) {
		String sqlCount = "select count(id) from player where serverid = " + key;
		String sql = "select * from player where serverid = ? order by connected desc, updated desc limit ?,?";
		Connection conn = null;
		List<Player> list = new ArrayList<Player>();
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			Statement stC = conn.createStatement();
			ResultSet rsC = stC.executeQuery(sqlCount);
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Player player = new Player();
				loadPlayer(player, rs);
				list.add(player);
			}
		} catch (SQLException e) {
			logger.error("findByServer: {}", e);
		} catch (IOException e) {
			logger.error("findByServer: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	/**
	 * List players ordered by connected and updated first
	 */
	public List<Player> findLatest(int offset, int limit, int[] count) {
		String sqlCount = "select count(id) from player";
		String sql = "select * from player order by connected desc, updated desc limit ?,?";
		Connection conn = null;
		List<Player> list = new ArrayList<Player>();
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
				Player player = new Player();
				loadPlayer(player, rs);
				list.add(player);
			}
		} catch (SQLException e) {
			logger.error("findLatest: {}", e);
		} catch (IOException e) {
			logger.error("findLatest: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	/**
	 * List banned players
	 */
	public List<Player> findBanned(int offset, int limit, int[] count) {
		String sqlCount = "select count(id) from player where baninfo is not null";
		String sql = "select * from player where baninfo is not null order by baninfo desc limit ?,?";
		Connection conn = null;
		List<Player> list = new ArrayList<Player>();
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
				Player player = new Player();
				loadPlayer(player, rs);
				list.add(player);
			}
		} catch (SQLException e) {
			logger.error("findBanned: {}", e);
		} catch (IOException e) {
			logger.error("findBanned: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	public Player get(Long key) throws EntityDoesNotExistsException {
		String sql = "select * from player where id = ?";
		Connection conn = null;
		Player player = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				player = new Player();
				loadPlayer(player, rs);
			} else {
				throw new EntityDoesNotExistsException("Player with id %s was not found", key.toString());
			}
		} catch (SQLException e) {
			logger.error("get: {}", e);
		} catch (IOException e) {
			logger.error("get: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return player;
	}
	/**
	 * @param player
	 * @param rs
	 * @throws SQLException 
	 */
	protected void loadPlayer(Player player, ResultSet rs) throws SQLException {
		player.setKey(rs.getLong("id"));
		player.setServer(rs.getLong("serverid"));
		player.setHash(rs.getString("guid"));
		player.setGuid(rs.getString("rguid"));
		player.setCreated(rs.getTimestamp("created"));
		player.setUpdated(rs.getTimestamp("updated"));
		player.setBanInfo(rs.getTimestamp("baninfo"));
		player.setClientId(rs.getLong("clientid"));
		player.setLevel(rs.getLong("level"));
		player.setNote(rs.getTimestamp("note"));
		player.setConnected(rs.getBoolean("connected"));
		player.setNickname(rs.getString("nickname"));
		player.setIp(rs.getString("ip"));
	}

	@Override
	public void save(Player player) {
		String sql;
		if (player.getKey() == null) {
			sql = "insert into player (serverid, guid, rguid, created, updated, baninfo, clientid, level, note, connected, nickname, ip) values (?,?,?,?,?,?,?,?,?,?,?,?)"; 
		} else {
			sql = "update player set serverid = ?," +
					"guid = ?," +
					"rguid = ?," +
					"created = ?," +
					"updated = ?," +
					"baninfo = ?," +
					"clientid = ?," +
					"level = ?," +
					"note = ?," +
					"connected = ?," +
					"nickname = ?," +
					"ip = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setLong(1, player.getServer());
			st.setString(2, player.getHash());
			if (player.getGuid() != null) st.setString(3, player.getGuid());
			else st.setNull(3, Types.VARCHAR);
			if (player.getCreated() == null) player.setCreated(new Date());
			if (player.getUpdated() == null) player.setUpdated(new Date());		
			st.setTimestamp(4, new java.sql.Timestamp(player.getCreated().getTime()));
			st.setTimestamp(5, new java.sql.Timestamp(player.getUpdated().getTime()));
			if (player.getBanInfo() != null) st.setTimestamp(6, new java.sql.Timestamp(player.getBanInfo().getTime()));
			else st.setNull(6, Types.DATE);
			st.setLong(7, player.getClientId());
			st.setLong(8, player.getLevel());
			if (player.getNote() != null) st.setTimestamp(9, new java.sql.Timestamp(player.getNote().getTime()));
			else st.setNull(9, Types.DATE);			
			st.setBoolean(10, player.isConnected());
			st.setString(11, player.getNickname());
			st.setString(12, player.getIp());
			if (player.getKey() != null) st.setLong(13, player.getKey());
			st.executeUpdate();
			if (player.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					player.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for player {}", player.getHash());
				}
			}
		} catch (SQLException e) {
			logger.error("Save: {}", e);
		} catch (IOException e) {
			logger.error("Save: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}

	}

	@Override
	public void save(Collection<Player> players) {
		for (Player player : players) {
			this.save(player);
		}
	}

	@Override
	public void cleanConnected(Long server) {
		String sql = "update player set connected = ? where connected = ? and serverid = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setBoolean(1, false);
			st.setBoolean(2, true);
			st.setLong(3, server);
			int r = st.executeUpdate();
			logger.debug("Updated {} players.", r);
		} catch (SQLException e) {
			logger.error("cleanConnected: {}", e);
		} catch (IOException e) {
			logger.error("cleanConnected: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public int countByServer(Long key, boolean connectedOnly) {
		String sql;
		if (connectedOnly) {
			sql = "select count(id) from player where serverid = ? and connected = ?";	
		} else {
			sql = "select count(id) from player where serverid = ?";
		}
		Connection conn = null;
		int c = 0;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			if (connectedOnly) st.setBoolean(2, true);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				c = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("countByServer: {}", e);
		} catch (IOException e) {
			logger.error("countByServer: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return c;
	}

	@Override
	public List<Player> findByClientId(Long clientId, int offset, int limit,
			int[] count) {
		String sqlCount = "select count(id) from player where clientid = ?";
		String sql = "select * from player where clientid = ? order by updated desc limit ?,?";
		Connection conn = null;
		List<Player> list = new ArrayList<Player>();
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement stC = conn.prepareStatement(sqlCount);
			stC.setLong(1, clientId);
			ResultSet rsC = stC.executeQuery();
			if (rsC.next()) {
				count[0] = rsC.getInt(1);
			}
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, clientId);
			st.setInt(2, offset);
			st.setInt(3, limit);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Player player = new Player();
				loadPlayer(player, rs);
				list.add(player);
			}
		} catch (SQLException e) {
			logger.error("findByClientId: {}", e);
		} catch (IOException e) {
			logger.error("findByClientId: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PlayerDAO#save(iddb.core.model.Player, boolean)
	 */
	@Override
	public void save(Player player, boolean commit) {
		if (commit) {
			save(player);
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PlayerDAO#save(java.util.Collection, boolean)
	 */
	@Override
	public void save(Collection<Player> players, boolean commit) {
		if (commit) {
			save(players);
		}
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PlayerDAO#findByOldKey(java.lang.String)
	 */
	@Override
	public Player findByOldKey(String key) throws EntityDoesNotExistsException {
		String sql = "select * from player where gaekey = ?";
		Connection conn = null;
		Player player = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				player = new Player();
				loadPlayer(player, rs);
			} else {
				throw new EntityDoesNotExistsException("Player with key %s was not found", key);
			}
		} catch (SQLException e) {
			logger.error("findByOldKey: {}", e);
		} catch (IOException e) {
			logger.error("findByOldKey: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return player;
	}

}
