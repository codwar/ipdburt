package jipdbs.xmlrpc;

import java.util.Date;

import jipdbs.JIPDBS;
import jipdbs.PlayerInfo;

public class JIPDBSRpc2Handler {

	private final JIPDBS app;
	
	public JIPDBSRpc2Handler(JIPDBS app) {
		this.app = app;
	}

	public void updateName(String key, String name, String version) {
		app.updateName(key, name, version, JIPDBSXmlRpc2Servlet.getClientIpAddress());
	}

	public void update(String key, Object[] plist) {
		// TODO
		for (Object o : plist) {
			PlayerInfo p = new PlayerInfo();
			Object[] values = ((Object[]) o);
			String event = (String) values[0];
			p.setName((String) values[1]);
			p.setGuid((String) values[2]);
			p.setId((String) values[3]);
			p.setIp((String) values[4]);
			p.setLevel((String) values[5]);
			if (values.length > 6) {
				p.setUpdated((Date) values[6]);
			} else {
				p.setUpdated(new Date());
			}
			if (values.length > 7) {
				p.setBaninfo((String) values[7]);
			}
			if ("connect".equalsIgnoreCase(event)) {
				// TODO
			} else if ("update".equalsIgnoreCase(event)) {
				// TODO
			} else if ("disconnect".equalsIgnoreCase(event)) {
				// TODO
			} else if ("banned".equalsIgnoreCase(event)) {
				// TODO
			}
		}

	}

}