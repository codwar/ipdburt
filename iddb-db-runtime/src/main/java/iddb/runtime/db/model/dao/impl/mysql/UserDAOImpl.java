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

import iddb.core.model.User;
import iddb.core.model.dao.UserDAO;
import iddb.core.util.Functions;
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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOImpl implements UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#save(iddb.core.model.User)
	 */
	@Override
	public void save(User user) {
		String sql;
		if (user.getKey() == null) {
			sql = "insert into user (loginid, roles, updated, created, password) values (?,?,?,?,?)"; 
		} else {
			sql = "update user set loginid = ?," +
					"roles = ?," +
					"updated = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getLoginId());
			st.setString(2, Functions.join(user.getRoles(), ","));
			st.setTimestamp(3, new Timestamp(new Date().getTime()));
			if (user.getKey() != null) {
				st.setLong(4, user.getKey());
			} else {
				st.setTimestamp(4, new Timestamp(new Date().getTime()));
				st.setString(5, user.getPassword());
			}
			st.executeUpdate();
			if (user.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					user.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id for user {}", user.getLoginId());
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
	 * @see iddb.core.model.dao.UserDAO#findAll(int, int, int[])
	 */
	@Override
	public List<User> findAll(int offset, int limit, int[] count) {
		String sqlCount = "select count(id) from user";
		String sql = "select * from user order by loginid limit ?,?";
		List<User> list = new ArrayList<User>();
		Connection conn = null;
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
				User user = new User();
				loadUser(user, rs);
				list.add(user);
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

	/**
	 * @param user
	 * @param rs
	 * @throws SQLException 
	 */
	private void loadUser(User user, ResultSet rs) throws SQLException {
		user.setKey(rs.getLong("id"));
		user.setLoginId(rs.getString("loginid"));
		user.setPassword(rs.getString("password"));
		user.setRoles(new LinkedHashSet<String>(Arrays.asList(rs.getString("roles").split(","))) );
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#get(java.lang.Long)
	 */
	@Override
	public User get(Long key) throws EntityDoesNotExistsException {
		String sql = "select * from user where id = ? limit 1";
		Connection conn = null;
		User user = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, key);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				user = new User();
				loadUser(user, rs);
			} else {
				throw new EntityDoesNotExistsException("User with id %s was not found", key);
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
		return user;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#get(java.lang.String)
	 */
	@Override
	public User get(String loginId) throws EntityDoesNotExistsException {
		String sql = "select * from user where loginid = ? limit 1";
		Connection conn = null;
		User user = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, loginId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				user = new User();
				loadUser(user, rs);
			} else {
				throw new EntityDoesNotExistsException("User with loginId %s was not found", loginId);
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
		return user;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.UserDAO#set_password(iddb.core.model.User, java.lang.String)
	 */
	@Override
	public void set_password(User user, String password) {
		String sql;
		sql = "update user set password = ?, updated = ? where id = ? limit 1";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, user.getPassword());
			st.setTimestamp(2, new Timestamp(new Date().getTime()));
			st.setLong(3, user.getKey());
			st.executeUpdate();
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

}
