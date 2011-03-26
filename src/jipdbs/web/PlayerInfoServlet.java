package jipdbs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.AliasResult;
import jipdbs.JIPDBS;
import jipdbs.PageLink;
import jipdbs.PlayerInfoView;
import jipdbs.data.Alias;
import jipdbs.data.Player;
import jipdbs.data.Server;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class PlayerInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 812345074174537109L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = req.getParameter("id");
		
		// TODO cachear por manejar los paginados
		
		Player player;
		try {
			player = app.getPlayer(id);
		} catch (EntityNotFoundException e1) {
			// TODO manejar y tirar 404
			e1.printStackTrace();
			throw new ServletException(e1);
		}
		
		int page = 1;
		int pageSize = 20;
		
		try {
			page = Integer.parseInt(req.getParameter("p"));
		} catch (NumberFormatException e) {
			// Ignore.
		}

		int offset = (page - 1) * pageSize;

		List<AliasResult> list = new ArrayList<AliasResult>();

		int[] total = new int[1];
		
		long time = System.currentTimeMillis();
		
		list = app.alias(id, offset, pageSize, total);
		
		Alias alias = app.getLastAlias(id);
		Server server;
		try {
			server = app.getServer(player.getServer());
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		PlayerInfoView infoView = new PlayerInfoView();
		infoView.setKey(id);
		infoView.setName(alias.getNickname());
		infoView.setIp(alias.getMaskedIp());
		infoView.setUpdated(alias.getUpdated());
		infoView.setServer(server);
		infoView.setBanInfo(player.getBanInfo());
		infoView.setAliases(list);
		
		time = System.currentTimeMillis() - time;
		
		int totalPages = (int) Math.ceil((double) total[0] / pageSize);
		
		req.setAttribute("player", infoView);
		req.setAttribute("count", total[0]);
		req.setAttribute("time", time);
		req.setAttribute("pageLink", new PageLink(page , pageSize, totalPages));
	}
}
