/**
 *   Copyright(c) 2010-2012 CodWar Soft
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

import iddb.core.model.LogModel;
import iddb.exception.EntityDoesNotExistsException;
import iddb.runtime.db.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogModelDAOImpl implements iddb.core.model.dao.LogModelDAO {

	private static Logger logger = LoggerFactory.getLogger(LogModelDAOImpl.class);
	
	@Override
	public void save(LogModel model) {
		String sql;
		if (model.getKey() == null) {
			sql = "insert into logmodel (message, created) values (?,?)"; 
		} else {
			sql = "update logmodel set message = ? where id = ? limit 1";
		}
		Connection conn = null;
		try {
			conn = ConnectionFactory.getMasterConnection();
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, model.getMessage());
			if (model.getKey() == null) {
				if (model.getCreated() == null) model.setCreated(new Date());
				st.setTimestamp(2, new java.sql.Timestamp(model.getCreated().getTime()));
			} else {
				st.setLong(2, model.getKey());
			}
			st.executeUpdate();
			if (model.getKey() == null) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs != null && rs.next()) {
					model.setKey(rs.getLong(1));
				} else {
					logger.warn("Couldn't get id");
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
	public List<LogModel> findByDate(Date dateFrom, Date dateTo) {
		String sql;
		sql = "SELECT id, message, created FROM logmodel WHERE created BETWEEN ? AND ?";
		List<LogModel> list = new ArrayList<LogModel>();
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setTimestamp(1, new java.sql.Timestamp(dateFrom.getTime()));
			st.setTimestamp(2, new java.sql.Timestamp(dateTo.getTime()));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				LogModel model = new LogModel();
				loadModel(model, rs);
				list.add(model);
			}
		} catch (SQLException e) {
			logger.error("findByDate: {}", e);
		} catch (IOException e) {
			logger.error("findByDate: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/**
	 * @param model
	 * @param rs
	 * @throws SQLException 
	 */
	private void loadModel(LogModel model, ResultSet rs) throws SQLException {
		model.setKey(rs.getLong(1));
		model.setMessage(rs.getString(2));
		model.setCreated(rs.getDate(3));
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws EntityDoesNotExistsException 
	 */
	@Override
	public LogModel get(Long id) throws EntityDoesNotExistsException {
		String sql;
		sql = "SELECT id, message, created FROM logmodel WHERE id = ?";
		Connection conn = null;
		LogModel model = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				model = new LogModel();
				loadModel(model, rs);
			} else {
				throw new EntityDoesNotExistsException("LogModel with id %s was not found", id.toString());
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
		return model;
	}
	
	@Override
	public void delete(Long id) throws EntityDoesNotExistsException {
		String sql;
		sql = "DELETE FROM logmodel WHERE id = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getSecondaryConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			logger.error("delete: {}", e);
		} catch (IOException e) {
			logger.error("delete: {}", e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}	
	
}
