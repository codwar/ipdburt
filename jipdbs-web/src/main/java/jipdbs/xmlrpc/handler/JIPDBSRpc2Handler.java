package jipdbs.xmlrpc.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.api.ServerManager;
import jipdbs.api.v2.Update;
import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;
import jipdbs.exception.UnauthorizedUpdateException;
import jipdbs.info.PlayerInfo;
import jipdbs.xmlrpc.JIPDBSXmlRpc2Servlet;

public class JIPDBSRpc2Handler {

	private static final Logger log = Logger.getLogger(JIPDBSRpc2Handler.class
			.getName());

	@SuppressWarnings("unused")
	private final JIPDBS app;
	private final Update updateApi;

	private static final int maxListSize = 30;
	
	public JIPDBSRpc2Handler(JIPDBS app) {
		this.app = app;
		this.updateApi = new Update();
	}

	public void updateName(String key, String name, String version) {
		this.updateApi.updateName(key, name, version, JIPDBSXmlRpc2Servlet.getClientIpAddress());
	}

	public void updateName(String key, String name, Object[] data) {
		this.updateApi.updateName(key, name, (String) data[0], JIPDBSXmlRpc2Servlet.getClientIpAddress());
	}
	
	public void update(String key, Object[] plist) throws Exception {

		try {
			Server server = ServerManager.getAuthorizedServer(key,
					JIPDBSXmlRpc2Servlet.getClientIpAddress());

			List<PlayerInfo> list = new ArrayList<PlayerInfo>();
			for (Object o : plist) {
				Object[] values = ((Object[]) o);
				PlayerInfo playerInfo = new PlayerInfo((String) values[0], (String) values[1], (String) values[2], parseLong(values[3]), (String) values[4], parseLong(values[5]));
				if (values.length > 6) {
					Date updated;
					if (values[6] instanceof Date) {
						updated = (Date) values[6];
					} else {
						try {
							updated = new Date((Integer) values[6] * 1000L);
						} catch (Exception e) {
							log.severe(e.getMessage());
							updated = new Date();
						}
					}
					playerInfo.setUpdated(updated);
				}
				if (values.length > 7) {
					playerInfo.setExtra((String) values[7]);
				}
				list.add(playerInfo);
			}
			if (list.size()>0) {
				if (list.size() > maxListSize) {
					log.warning("List size is " + Integer.toString(list.size()));
					// this is too much to process
					list = list.subList(list.size() - maxListSize, list.size());
				} else {
					log.info("List size is " + Integer.toString(list.size()));
				}
				updateApi.updatePlayer(server, list);	
			} else {
				if (server.getOnlinePlayers()>0) {
					log.fine("Cleaning server " + server.getName());
					updateApi.cleanServer(server);
				}
			}
		} catch (UnauthorizedUpdateException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
			throw e;
		}
	}

	private Long parseLong(Object s) {
		try {
			if (s instanceof String) {
				return Long.parseLong((String) s);
			} else if (s instanceof Number) {
				return ((Number) s).longValue();
			}
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}