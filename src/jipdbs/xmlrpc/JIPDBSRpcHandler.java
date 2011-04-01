package jipdbs.xmlrpc;

import java.util.ArrayList;
import java.util.List;

import jipdbs.BanInfo;
import jipdbs.JIPDBS;
import jipdbs.PlayerInfo;

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

			PlayerInfo p = new PlayerInfo();
			p.setName((String) values[0]);
			p.setIp((String) values[1]);
			p.setGuid((String) values[2]);

			list.add(p);

		}
		if (list.size() > 0) app.updateConnect(key, list, JIPDBSXmlRpcServlet.getClientIpAddress());
	}

	public void updateDisconnect(String key, Object[] plist) {

		List<PlayerInfo> list = new ArrayList<PlayerInfo>();

		for (Object o : plist) {

			Object[] values = ((Object[]) o);

			PlayerInfo p = new PlayerInfo();
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
			
			if (values.length == 4) {
				BanInfo b = new BanInfo();
				b.setGuid((String) values[0]);
				b.setReason((String) values[1]);
				b.setName((String) values[2]);
				b.setIp((String) values[3]);
				list.add(b);
			}

		}
		if (list.size() > 0) app.updateBanInfo(key, list, JIPDBSXmlRpcServlet.getClientIpAddress());
	}
}