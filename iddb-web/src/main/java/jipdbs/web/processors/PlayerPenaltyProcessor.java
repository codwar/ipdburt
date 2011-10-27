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

import iddb.api.RemotePermissions;
import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.util.Functions;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.service.UserServiceFactory;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.SimpleEntry;
import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.utils.UrlReverse;

public class PlayerPenaltyProcessor extends SimpleActionProcessor {

	private static final Logger log = LoggerFactory.getLogger(PlayerPenaltyProcessor.class);
	
	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doProcess(ResolverContext ctx)
			throws ProcessorException {

		IDDBService app = (IDDBService) ctx.getServletContext().getAttribute("jipdbs");
		HttpServletRequest req = ctx.getRequest();
		
		String playerId = ctx.getRequest().getParameter("k");
		String type = ctx.getParameter("type");
		String reason = ctx.getRequest().getParameter("reason");
		String duration = ctx.getRequest().getParameter("duration");
		String durationType = ctx.getRequest().getParameter("dt");
		
		UrlReverse reverse = new UrlReverse(ctx.getServletContext());
		String redirect;
		try {
			redirect = reverse.resolve("playerinfo", new Entry[]{new SimpleEntry("key", playerId)});
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ProcessorException(e);
		}
		
		Player player = null;
		try {
			player = app.getPlayer(playerId);
		} catch (EntityDoesNotExistsException e) {
			log.error(e.getMessage());
			throw new HttpError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		Server server;
		try {
			server = app.getServer(player.getServer(), true);
		} catch (EntityDoesNotExistsException e) {
			log.error(e.getMessage());
			throw new ProcessorException(e);
		}
		
		if (!UserServiceFactory.getUserService().hasPermission(server.getKey(), server.getAdminLevel())) {
			Flash.error(req, MessageResource.getMessage("forbidden"));
			log.debug("Forbidden");
			throw new HttpError(HttpServletResponse.SC_FORBIDDEN);
		}
		
		Player currentPlayer = null;
		if (!UserServiceFactory.getUserService().getCurrentUser().isSuperAdmin()) {
			currentPlayer = UserServiceFactory.getUserService().getSubjectPlayer(player.getServer());
			if (currentPlayer == null) {
				throw new HttpError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		
		if (StringUtils.isEmpty(reason)) {
			Flash.error(req, MessageResource.getMessage("reason_field_required"));
			return redirect;
		}
		
		Penalty penalty = new Penalty();
		penalty.setReason(reason);
		penalty.setPlayer(player.getKey());
		penalty.setActive(true);
		
		if (currentPlayer != null) penalty.setAdmin(currentPlayer.getKey());
		
		if (type.equals("notice")) {
			penalty.setType(Penalty.NOTICE);
			if ((server.getPermission() & RemotePermissions.ADD_NOTICE) == RemotePermissions.ADD_NOTICE) {
				penalty.setSynced(false);
				Flash.info(req, MessageResource.getMessage("local_action_pending"));
			} else {
				penalty.setSynced(true);
				Flash.warn(req, MessageResource.getMessage("local_action_only"));
			}
		} else {
			Long dm = Functions.time2minutes(duration + durationType);
			if (dm == 0L) {
				Flash.error(req, MessageResource.getMessage("duration_field_required"));
				return redirect;			
			}
			if ((server.getPermission() & RemotePermissions.ADD_BAN) == RemotePermissions.ADD_BAN) {
				penalty.setSynced(false);
				Flash.info(req, MessageResource.getMessage("local_action_pending"));
			} else {
				Flash.error(req, MessageResource.getMessage("remote_action_not_available"));
				return redirect;
			}
			penalty.setType(Penalty.BAN);
			penalty.setDuration(dm);
		}

		try {
			app.addPenalty(penalty, !penalty.getSynced());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return redirect;
	}

}
