package jipdbs.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.api.Events;
import jipdbs.core.JIPDBS;
import jipdbs.core.data.Server;
import jipdbs.info.PlayerInfo;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class JIPDBSHTTPHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 581879616460472029L;
	private static final Logger log = Logger.getLogger(JIPDBSHTTPHandler.class.getName());
	
	private JIPDBS app;
	
	@Override
	public void init() throws ServletException {
		this.app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if ("/insertLog".equalsIgnoreCase(req.getPathInfo())) {
			List<PlayerInfo> list = new ArrayList<PlayerInfo>();
			String key = req.getParameter("key");
			JSONArray players = (JSONArray) JSONValue.parse(req.getParameter("players"));
			PlayerInfo p;
			for (Object obPlayer : players) {
				JSONArray player = (JSONArray) obPlayer;
				p = new PlayerInfo(Events.UPDATE, ((String) player.get(0)).trim(), ((String) player.get(2)).trim(), null, ((String) player.get(1)).trim(), null);
				list.add(p);
			}
			app.updateConnect(key, list, null);
		} else if ("/fetchserver".equalsIgnoreCase(req.getPathInfo())) {
			resp.setContentType("text/plain");
			String key = req.getParameter("key");
			Server server = null;
			try {
				server = app.getServer(key);
			} catch (EntityNotFoundException e) {
				log.severe(e.getMessage());
			}
			if (server == null) {
				resp.getWriter().println("{\"error\": true, \"server\": {\"key\": \""+key+"\"}}");
			} else {
				if (server.getDirty()) {
					app.refreshServerInfo(server);
				}
				resp.getWriter().println("{\"error\": false, \"server\": {\"key\": \""+server.getKeyString()+"\", \"count\": \""+server.getOnlinePlayers()+"\",\"name\": \""+server.getName()+"\"}}");
			}
		}
	}
}
