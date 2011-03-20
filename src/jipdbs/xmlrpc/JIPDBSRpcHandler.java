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
		// TODO hacer configurable la verificación de IP a nivel servidor
		app.updateName(key, name, null); //JIPDBSXmlRpcServlet.getClientIpAddress());
	}

	public void insertLog(String key, Object[] plist) {

		List<PlayerInfo> list = new ArrayList<PlayerInfo>();

		for (Object o : plist) {

			Object[] values = ((Object[]) o);

			PlayerInfo p = new PlayerInfo();
			p.setName((String) values[0]);
			p.setIp((String) values[1]);
			p.setGuid((String) values[2]);

			list.add(p);

		}

		app.insertLog(key, list, null); //JIPDBSXmlRpcServlet.getClientIpAddress());
	}

	public void updateBanInfo(String key, Object[] plist) {

		List<BanInfo> list = new ArrayList<BanInfo>();

		for (Object o : plist) {

			Object[] values = ((Object[]) o);

			BanInfo b = new BanInfo();
			b.setGuid((String) values[0]);
			b.setReason((String) values[1]);

			list.add(b);

		}

		app.updateBanInfo(key, list, null); //JIPDBSXmlRpcServlet.getClientIpAddress());
	}
}