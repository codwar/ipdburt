package iddb.cli.command;

import iddb.api.RemotePermissions;
import iddb.cli.Command;
import iddb.cli.ConnectionFactory;
import iddb.core.DAOException;
import iddb.core.model.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class UpdateBanDuration extends Command {

	@Override
	protected void execute(OptionSet options) throws Exception {

		int count = 0;
		int skipped = 0;
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			
			System.out.println("Processing ...");

			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("select id, maxban from server");

			while (rs.next()) {
				count++;
				Long id = rs.getLong("id");
				Long maxBan = rs.getLong("maxban");
				if (maxBan > 0) {
					Server server = new Server();
					server.setKey(id);
					loadPermissions(server);
					if (server.getBanPermissions() == null || server.getBanPermissions().size() == 0) {
						for (Integer lv = server.getPermission(RemotePermissions.ADD_BAN); lv <= 100; lv += 20) {
							server.setBanPermission(lv.longValue(), maxBan);
						}
						saveBanPermissions(server);
					}
				} else {
					skipped++;
				}
				
			}
			
			System.out.println("Processed: " + count);
			System.out.println("Skipped: " + skipped);
			rs.close();
			statement.close();
		} finally {
			if (conn != null) conn.close();
		}  

	}

	private void saveBanPermissions(Server server) throws DAOException {
		String sqlI = "insert into server_ban_perm (serverid, level, value) values (?,?,?)";
		String sqlD = "delete from server_ban_perm where serverid = ?";
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			// DELETE PREVIOUS RECORDS
			PreparedStatement stD = conn.prepareStatement(sqlD);
			stD.setLong(1, server.getKey());
			stD.executeUpdate();
			
			PreparedStatement st;
			
			for (Entry<Long, Long> entry : server.getBanPermissions().entrySet()) {
				st = conn.prepareStatement(sqlI);
				st.setLong(1, server.getKey());
				st.setInt(2, entry.getKey().intValue());
				st.setLong(3, entry.getValue());
				st.executeUpdate();
			}			
			
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
			throw new DAOException(e.getMessage());
		} catch (IOException e) {
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(false);
					conn.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	private void loadPermissions(Server server) throws DAOException {
		Connection conn = null;
		String sql;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement st;;
			ResultSet rs;
			// func permissions
			sql = "select * from server_permission where serverid = ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, server.getKey());
			rs = st.executeQuery();
			server.setPermissions(new HashMap<Long, Integer>());
			while (rs.next()) {
				server.getPermissions().put(new Long(rs.getInt("funcid")), rs.getInt("level"));
			}
			rs.close();
			st.close();
			// ban permissions
			sql = "select * from server_ban_perm where serverid = ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, server.getKey());
			rs = st.executeQuery();
			while (rs.next()) {
				server.setBanPermission(new Long(rs.getInt("level")), rs.getLong("value"));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		} catch (IOException e) {
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
			}
		}
	}
	
	@Override
	public OptionParser getCommandOptions() {
		return new OptionParser();
	}

}
