package jipdbs.rpc.handlers;

import java.util.Collection;

public class InfoUpdater {
	
	public Boolean updateName(String key, String name) {
		System.out.println("Name update " + name);
		return new Boolean(true);
	}
	
	public Boolean insertLog(String key, Collection<Object> clients) {
		return new Boolean(true);
	}
}
