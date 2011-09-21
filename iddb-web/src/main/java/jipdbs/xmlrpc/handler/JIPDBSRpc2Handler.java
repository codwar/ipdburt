package jipdbs.xmlrpc.handler;

import iddb.api.Events;
import iddb.api.ServerManager;
import iddb.api.v2.Update;
import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.core.model.Server;
import iddb.exception.UnauthorizedUpdateException;
import iddb.info.PenaltyInfo;
import iddb.info.PlayerInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.xmlrpc.JIPDBSXmlRpc2Servlet;

public class JIPDBSRpc2Handler {

	private static final Logger log = Logger.getLogger(JIPDBSRpc2Handler.class
			.getName());

	@SuppressWarnings("unused")
	private final IDDBService app;
	private final Update updateApi;

	private static final int maxListSize = 30;
	
	public JIPDBSRpc2Handler(IDDBService app) {
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
				String event = (String) values[0];
				PlayerInfo playerInfo = new PlayerInfo(event, (String) values[1], (String) values[2], parseLong(values[3]), (String) values[4], parseLong(values[5]));
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
					PenaltyInfo info = new PenaltyInfo();
					if (Events.BAN.equals(event)) {
						String[] parts = ((String) values[7]).split("::");
						info.setType(Penalty.BAN);
						info.setCreated(parseLong(parts[1]));
						info.setDuration(parseLong(parts[2]));
						info.setReason(parts[3]);
					} else {
						info.setCreated(new Date());
						info.setDuration(-1L);
						info.setType(Penalty.NOTICE);
						info.setReason((String) values[7]);
					}
					playerInfo.setPenaltyInfo(info);
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