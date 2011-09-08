package jipdbs.web.processors;

import iddb.core.JIPDBS;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.util.Functions;
import iddb.info.AliasResult;
import iddb.info.PenaltyInfo;
import iddb.info.PlayerInfoView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class PlayerInfoProcessor extends FlashResponseProcessor {

	private static final Logger log = Logger.getLogger(PlayerInfoProcessor.class.getName());
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String id = context.getParameter("key");
		
		Player player;
		try {
			player = app.getPlayer(id);
		} catch (Exception e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
			throw new HttpError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		List<AliasResult> list = new ArrayList<AliasResult>();
		
		Server server;
		try {
			server = app.getServer(player.getServer());
		} catch (EntityNotFoundException e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
			throw new ProcessorException(e);
		}
		
		PlayerInfoView infoView = new PlayerInfoView();
		infoView.setKey(id);
		infoView.setName(player.getNickname());
		if (app.isSuperAdmin()) {
			infoView.setIp(player.getIp());	
		} else {
			infoView.setIp(Functions.maskIpAddress(player.getIp()));
		}
		infoView.setUpdated(player.getUpdated());
		infoView.setServer(server);
		infoView.setBanInfo(PenaltyInfo.getDetail(player.getBanInfo()));
		infoView.setAliases(list);
		infoView.setClientId(player.getClientId() != null ? "@" + player.getClientId().toString() : "-");
		infoView.setPlaying(player.isConnected());
		infoView.setNote(player.getNote());
		if (player.getLevel() != null && player.getLevel() > 0 && player.getLevel() <= server.getMaxLevel()) {
			infoView.setLevel(player.getLevel().toString());
		} else {
			infoView.setLevel("-");
		}
		
		req.setAttribute("player", infoView);
	
		return null;
	}

}
