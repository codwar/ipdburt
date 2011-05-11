package jipdbs.xmlrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jipdbs.BanInfo;
import jipdbs.JIPDBS;
import jipdbs.api.Events;
import jipdbs.bean.PlayerInfo;
import jipdbs.xmlrpc.JIPDBSXmlRpcServlet;

public class JIPDBSRpcHandler {

	private final JIPDBS app;
	
	public JIPDBSRpcHandler(JIPDBS app) {
		this.app = app;
	}

	public void updateName(String key, String name) {
		app.updateName(key, name, JIPDBSXmlRpcServlet.getClientIpAddress());
	}

	public void updateConnect(String key, Object[] plist) {

		List<PlayerInfo> list = new ArrayList<PlayerInfo>();
		
		for (Object o : plist) {

			Object[] values = ((Object[]) o);
			PlayerInfo p = new PlayerInfo(Events.CONNECT, (String) values[0], (String) values[2], null, (String) values[1], null);
			if (values.length == 4) {
				p.setUpdated((Date) values[3]);
			}
			list.add(p);
		}
		if (list.size() > 0) app.updateConnect(key, list, JIPDBSXmlRpcServlet.getClientIpAddress());
	}

	public void updateDisconnect(String key, Object[] plist) {

		List<PlayerInfo> list = new ArrayList<PlayerInfo>();

		for (Object o : plist) {

			Object[] values = ((Object[]) o);
			PlayerInfo p = new PlayerInfo(Events.CONNECT, (String) values[0], (String) values[2], null, (String) values[1], null);
			p.setName((String) values[0]);
			p.setIp((String) values[1]);
			p.setGuid((String) values[2]);

			list.add(p);

		}

		if (list.size() > 0) app.updateDisconnect(key, list, JIPDBSXmlRpcServlet.getClientIpAddress());
	}
	
	public void updateBanInfo(String key, Object[] plist) {

		List<BanInfo> list = new ArrayList<BanInfo>();

		for (Object o : plist) {

			Object[] values = ((Object[]) o);
			
			if (values.length >= 4) {
				BanInfo b = new BanInfo();
				b.setGuid((String) values[0]);
				b.setReason((String) values[1]);
				b.setName((String) values[2]);
				b.setIp((String) values[3]);
				if (values.length > 4) {
					b.setUpdated((Date) values[4]);
				}
				list.add(b);
			}

		}
		if (list.size() > 0) app.updateBanInfo(key, list, JIPDBSXmlRpcServlet.getClientIpAddress());
	}	
}