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
package iddb.web.security.dao;

import iddb.exception.EntityDoesNotExistsException;
import iddb.runtime.db.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionDAO {

	private static final Logger log = LoggerFactory.getLogger(SessionDAO.class);
	
	public void insert(Session session) {
		String sql;
		sql = "insert into user_session (id, userid, ip, created) values (?,?,?,?)"; 
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, session.getKey());
			st.setLong(2, session.getUserId());
			st.setString(3, session.getIp());
			st.setTimestamp(4, new Timestamp(new Date().getTime()));
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("insert", e);
		} catch (IOException e) {
			log.error("insert", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}

	public void delete(String key) {
		String sql;
		sql = "delete from user_session where id = ?"; 
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, key);
			int r = st.executeUpdate();
			log.debug("Removed {} sessions", Integer.toString(r));
		} catch (SQLException e) {
			log.error("delete", e);
		} catch (IOException e) {
			log.error("delete", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}		
	}
	
	public void cleanUp(Long expire) {
		String sql;
		sql = "delete from user_session where created < ?"; 
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setTimestamp(1, new Timestamp(DateUtils.addDays(new Date(), expire.intValue() * -1).getTime()));
			int r = st.executeUpdate();
			log.debug("Removed {} sessions", Integer.toString(r));
		} catch (SQLException e) {
			log.error("cleanUp", e);
		} catch (IOException e) {
			log.error("cleanUp", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}		
	}
	
	public Session get(String key) throws EntityDoesNotExistsException {
		String sql = "select * from user_session where id = ?";
		Connection conn = null;
		Session session = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				session = new Session();
				loadSession(session, rs);
			} else {
				throw new EntityDoesNotExistsException("Session with key %s was not found", key);
			}
		} catch (SQLException e) {
			log.error("get", e);
		} catch (IOException e) {
			log.error("get", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return session;		
	}
	
	/**
	 * @param session
	 * @param rs
	 * @throws SQLException 
	 */
	private void loadSession(Session session, ResultSet rs) throws SQLException {
		session.setKey(rs.getString("id"));
		session.setUserId(rs.getLong("userid"));
		session.setIp(rs.getString("ip"));
		session.setCreated(rs.getDate("created"));
	}

	public Session get(String key, Long userId, String ip) throws EntityDoesNotExistsException {
		String sql = "select * from user_session where id = ? and userid = ? and ip = ?";
		Connection conn = null;
		Session session = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, key);
			st.setLong(2, userId);
			st.setString(3, ip);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				session = new Session();
				loadSession(session, rs);
			} else {
				throw new EntityDoesNotExistsException("Session with key %s was not found", key);
			}
		} catch (SQLException e) {
			log.error("get", e);
		} catch (IOException e) {
			log.error("get", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return session;		
	}
	
}
