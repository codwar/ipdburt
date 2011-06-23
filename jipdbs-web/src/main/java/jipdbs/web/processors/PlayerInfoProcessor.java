package jipdbs.web.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.core.JIPDBS;
import jipdbs.core.model.Player;
import jipdbs.core.model.Server;
import jipdbs.core.util.Functions;
import jipdbs.info.AliasResult;
import jipdbs.info.PenaltyInfo;
import jipdbs.info.PlayerInfoView;
import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class PlayerInfoProcessor extends Processor {

	private static final Logger log = Logger.getLogger(PlayerInfoProcessor.class.getName());
	
	@Override
	public void doProcess(ResolverContext context) throws ProcessorException {
		
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
			try {
				context.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} catch (IOException e1) {
				throw new ProcessorException(e);
			}
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
		infoView.setIp(Functions.maskIpAddress(player.getIp()));
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
		
	}

}
