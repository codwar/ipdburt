/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package jipdbs.web.processors;

import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.core.model.PenaltyHistory;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.util.Functions;
import iddb.exception.EntityDoesNotExistsException;
import iddb.info.AliasResult;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.viewbean.NoticeViewBean;
import iddb.web.viewbean.PenaltyEventViewBean;
import iddb.web.viewbean.PenaltyViewBean;
import iddb.web.viewbean.PlayerViewBean;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.MessageResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class PlayerInfoProcessor extends ResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(PlayerInfoProcessor.class);
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String id = context.getParameter("key");
		
		Player player;
		try {
			player = app.getPlayer(id);
		} catch (Exception e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw new HttpError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		Server server;
		try {
			server = app.getServer(player.getServer(), true);
		} catch (EntityDoesNotExistsException e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw new ProcessorException(e);
		}
		
		Integer minLevel = server.getAdminLevel();
		Boolean hasAdmin = UserServiceFactory.getUserService().hasAnyServer(minLevel);
		Boolean hasServerAdmin = UserServiceFactory.getUserService().hasPermission(server.getKey());
		
		List<NoticeViewBean> notices = null;
		List<PenaltyEventViewBean> events = null;
		
		PlayerViewBean infoView = new PlayerViewBean();
		infoView.setKey(player.getKey().toString());
		infoView.setName(player.getNickname());
		if (hasAdmin) {
			infoView.setGuid(player.getGuid());
			infoView.setIp(player.getIp());	
		} else {
			infoView.setIp(Functions.maskIpAddress(player.getIp()));
		}
		infoView.setUpdated(player.getUpdated());
		infoView.setServer(server);
		
		if (hasAdmin) {
			Penalty ban = app.getLastPenalty(player);
			if (ban != null) {
				PenaltyViewBean penaltyViewBean = new PenaltyViewBean();
				penaltyViewBean.setKey(ban.getKey());
				penaltyViewBean.setCreated(ban.getCreated());
				penaltyViewBean.setDuration(ban.getDuration());
				penaltyViewBean.setReason(ban.getReason());
				if (ban.getAdmin() != null) {
					try {
						Player admin = app.getPlayer(ban.getAdmin());
						penaltyViewBean.setAdmin(admin.getNickname());
					} catch (EntityDoesNotExistsException e) {
						log.warn(e.getMessage());
					}
				}
				infoView.setBanInfo(penaltyViewBean.toString());
			}
			
			List<Penalty> pn = app.getActivePenalties(player.getKey(), Penalty.NOTICE);
			notices = new ArrayList<NoticeViewBean>();
			for (Penalty notice : pn) {
				NoticeViewBean noticeViewBean = new NoticeViewBean();
				noticeViewBean.setKey(notice.getKey());
				noticeViewBean.setCreated(notice.getCreated());
				noticeViewBean.setReason(notice.getReason());
				if (notice.getAdmin() != null) {
					try {
						Player admin = app.getPlayer(notice.getAdmin());
						noticeViewBean.setAdmin(admin.getNickname());
					} catch (EntityDoesNotExistsException e) {
						log.warn(e.getMessage());
					}
				}
				notices.add(noticeViewBean);
			}
			List<PenaltyHistory> historyList = app.listPenaltyEvents(player.getKey(), 10);
			events = new ArrayList<PenaltyEventViewBean>();
			for (PenaltyHistory history : historyList) {
				PenaltyEventViewBean event = new PenaltyEventViewBean();
				event.setStatus(MessageResource.getMessage("st_msg_" + history.getStatus().toString()));
				event.setUpdated(history.getUpdated());
				try {
					Player pa = app.getPlayer(history.getAdminId());
					event.setAdmin(pa.getNickname());
				} catch (EntityDoesNotExistsException e) {
					event.setAdmin("-");
				}
				try {
					Penalty pe = app.getPenalty(history.getPenaltyId());
					if (pe.getType() == Penalty.BAN) {
						if (pe.getActive()) {
							event.setType(MessageResource.getMessage("event_ban"));	
						} else {
							event.setType(MessageResource.getMessage("event_unban"));
						}
					} else {
						if (pe.getActive()) {
							event.setType(MessageResource.getMessage("event_note"));	
						} else {
							event.setType(MessageResource.getMessage("event_delnote"));
						}					
					}
				} catch (EntityDoesNotExistsException e) {
					event.setType("-");
				}
				events.add(event);
			}
			
		} else {
			if (player.getBanInfo() != null) {
				infoView.setBanInfo(MessageResource.getMessage("banned"));
			}
		}
		infoView.setAliases(new ArrayList<AliasResult>());
		infoView.setClientId(player.getClientId() != null ? "@" + player.getClientId().toString() : "-");
		infoView.setPlaying(player.isConnected());
		
		if (hasAdmin && player.getLevel() != null) {
			infoView.setLevel(player.getLevel().toString());
		} else {
			infoView.setLevel("-");
		}
		
		req.setAttribute("player", infoView);
		req.setAttribute("server", server);
		req.setAttribute("notices", notices);
		req.setAttribute("hasAdmin", hasAdmin);
		req.setAttribute("hasServerAdmin", hasServerAdmin);
		req.setAttribute("permission", server.getPermission());
	
		return null;
	}

}
