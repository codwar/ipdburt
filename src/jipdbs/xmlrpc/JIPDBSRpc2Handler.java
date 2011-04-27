package jipdbs.xmlrpc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Logger;

import jipdbs.JIPDBS;
import jipdbs.UnauthorizedUpdateException;
import jipdbs.data.Server;
import jipdbs.util.MailAdmin;

public class JIPDBSRpc2Handler {

	private static final Logger log = Logger.getLogger(JIPDBSRpc2Handler.class
			.getName());

	private final JIPDBS app;

	public JIPDBSRpc2Handler(JIPDBS app) {
		this.app = app;
	}

	public void updateName(String key, String name, String version) {
		app.updateName(key, name, version,
				JIPDBSXmlRpc2Servlet.getClientIpAddress());
	}

	public void update(String key, Object[] plist) {

		try {
			Server server = app.getAuthorizedServer(key,
					JIPDBSXmlRpc2Servlet.getClientIpAddress());

			for (Object o : plist) {
				Object[] values = ((Object[]) o);
				String event = (String) values[0];
				String name = (String) values[1];
				String guid = (String) values[2];
				Integer id = parseInteger((String) values[3]);
				String ip = (String) values[4];
				Integer level = parseInteger((String) values[5]);

				Date updated = new Date();

				if (values.length > 6)
					updated = (Date) values[6];

				String baninfo = null;

				if (values.length > 7)
					baninfo = (String) values[7];

				if ("connect".equalsIgnoreCase(event)) {

					app.playerConnected(server, name, ip, guid, updated, id,
							level);

				} else if ("update".equalsIgnoreCase(event)) {

					app.playerUpdated(server, name, ip, guid, updated, id,
							level);

				} else if ("disconnect".equalsIgnoreCase(event)) {

					app.playerDisconnected(server, name, ip, guid, updated, id,
							level);

				} else if ("banned".equalsIgnoreCase(event)) {

					app.playerBanned(server, name, ip, guid, updated, id,
							level, baninfo);
				} else if ("unbanned".equalsIgnoreCase(event)) {

					app.playerUnbanned(server, name, ip, guid, updated, id,
							level);
				}
			}

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