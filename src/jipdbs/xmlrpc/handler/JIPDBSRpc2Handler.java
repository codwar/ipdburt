package jipdbs.xmlrpc.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.JIPDBS;
import jipdbs.api.ServerManager;
import jipdbs.api.v2.Update;
import jipdbs.bean.PlayerInfo;
import jipdbs.data.Server;
import jipdbs.exception.UnauthorizedUpdateException;
import jipdbs.util.MailAdmin;
import jipdbs.xmlrpc.JIPDBSXmlRpc2Servlet;

public class JIPDBSRpc2Handler {

	private static final Logger log = Logger.getLogger(JIPDBSRpc2Handler.class
			.getName());

	private final JIPDBS app;
	private final Update updateApi;
	
	public JIPDBSRpc2Handler(JIPDBS app) {
		this.app = app;
		this.updateApi = new Update();
	}

	public void updateName(String key, String name, String version) {
		this.updateApi.updateName(key, name, version, JIPDBSXmlRpc2Servlet.getClientIpAddress());
	}

	public void update(String key, Object[] plist) {

		try {
			ServerManager serverManager = new ServerManager();
			Server server = serverManager.getAuthorizedServer(key,
					JIPDBSXmlRpc2Servlet.getClientIpAddress());

			List<PlayerInfo> list = new ArrayList<PlayerInfo>();
			for (Object o : plist) {
				Object[] values = ((Object[]) o);
				PlayerInfo playerInfo = new PlayerInfo((String) values[0], (String) values[1], (String) values[2], parseInteger((String) values[3]), (String) values[4], parseInteger((String) values[5]));
				if (values.length > 6)
					playerInfo.setUpdated((Date) values[6]);
				if (values.length > 7)
					playerInfo.setExtra((String) values[7]);
				list.add(playerInfo);
			}
			updateApi.updatePlayer(server, list);
			
		} catch (UnauthorizedUpdateException e) {
			MailAdmin.sendMail("WARN", e.getMessage());
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	private Integer parseInteger(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}