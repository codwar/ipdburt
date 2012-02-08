package iddb.cli.command;

import iddb.cli.Command;
import iddb.cli.ConnectionFactory;
import iddb.core.util.Functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class BuildNameIndex extends Command {

	/**
	 * 
	 */
	static int count = 0;

	@Override
	protected void execute(OptionSet options) throws Exception {

		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			
			System.out.println("Processing ...");
			
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("select id, nickname from alias");
			
			String sql = "update alias set nameindex = ? where id = ? limit 1";
			conn.setAutoCommit(false);
			PreparedStatement pst = conn.prepareStatement(sql);
			while (rs.next()) {
				count++;
				String nickname = rs.getString("nickname");
				pst.setString(1, Functions.createNameIndex(nickname));
				pst.setLong(2, rs.getLong("id"));
				pst.addBatch();
				if (count % 500 == 0) {
					System.out.println("Processed " + count + " id " + rs.getLong("id"));
				}
				if (count % 1500 == 0) {
					pst.executeBatch();
					conn.commit();
				}
			}
			pst.executeBatch();
			conn.commit();
			
			System.out.println("Processed: " + count);
			rs.close();
			statement.close();
		} finally {
			if (conn != null) conn.close();
		}  

	}

	@Override
	public OptionParser getCommandOptions() {
		return new OptionParser();
	}

}
