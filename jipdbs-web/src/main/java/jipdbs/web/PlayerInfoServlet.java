package jipdbs.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.api.v2.Update;
import jipdbs.core.JIPDBS;
import jipdbs.core.model.Player;
import jipdbs.core.model.Server;
import jipdbs.core.util.Functions;
import jipdbs.info.AliasResult;
import jipdbs.info.BanInfo;
import jipdbs.info.PlayerInfoView;

import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PlayerInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 812345074174537109L;

	private static final Logger log = Logger.getLogger(Update.class.getName());
	
	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = req.getParameter("id");
		
		Player player;
		try {
			player = app.getPlayer(id);
		} catch (Exception e) {
			// TODO manejar y tirar 404
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
			throw new ServletException(e);
		}
		
		List<AliasResult> list = new ArrayList<AliasResult>();
		
		Server server;
		try {
			server = app.getServer(player.getServer());
		} catch (EntityNotFoundException e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
			throw new ServletException(e);
		}
		
        UserService userService = UserServiceFactory.getUserService();
        
		PlayerInfoView infoView = new PlayerInfoView();
		infoView.setKey(id);
		infoView.setName(player.getNickname());
        if (userService.isUserAdmin()) {
            infoView.setIp(player.getIp());    
        } else {
            infoView.setIp(Functions.maskIpAddress(player.getIp()));    
        }
		infoView.setUpdated(player.getUpdated());
		infoView.setServer(server);
		infoView.setBanInfo(BanInfo.getDetail(player.getBanInfo()));
		infoView.setAliases(list);
		infoView.setClientId(player.getClientId() != null ? "@" + player.getClientId().toString() : "-");
		infoView.setPlaying(player.isConnected());
		infoView.setNote(player.getNote());
		if (player.getLevel() != null && player.getLevel() > 0 && player.getLevel() <= server.getMaxLevel()) {
			infoView.setLevel(player.getLevel().toString());
		} else {
			infoView.setLevel("-");
		}
		
		//int totalPages = (int) Math.ceil((double) total[0] / pageSize);
		
		req.setAttribute("player", infoView);
		//req.setAttribute("count", total[0]);
		//req.setAttribute("time", time);
		//req.setAttribute("pageLink", new PageLink(page , pageSize, totalPages));
	}
}
