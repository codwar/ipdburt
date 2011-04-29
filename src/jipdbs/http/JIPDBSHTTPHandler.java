package jipdbs.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;
import jipdbs.api.Events;
import jipdbs.bean.PlayerInfo;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class JIPDBSHTTPHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 581879616460472029L;

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
		}
	}
}
