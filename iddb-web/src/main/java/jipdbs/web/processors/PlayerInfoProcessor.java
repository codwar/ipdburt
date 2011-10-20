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
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.util.Functions;
import iddb.exception.EntityDoesNotExistsException;
import iddb.info.AliasResult;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.viewbean.NoticeViewBean;
import iddb.web.viewbean.PenaltyViewBean;
import iddb.web.viewbean.PlayerViewBean;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.CommonConstants;
import jipdbs.web.MessageResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class PlayerInfoProcessor extends FlashResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(PlayerInfoProcessor.class);
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
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
			server = app.getServer(player.getServer());
		} catch (EntityDoesNotExistsException e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw new ProcessorException(e);
		}

		Boolean hasAdmin = UserServiceFactory.getUserService().hasAnyServer(CommonConstants.ADMIN_LEVEL); 
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
			Penalty notice = app.getLastNotice(player);
			if (notice != null) {
				NoticeViewBean noticeViewBean = new NoticeViewBean();
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
				infoView.setNote(noticeViewBean.toString());
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
	
		return null;
	}

}
