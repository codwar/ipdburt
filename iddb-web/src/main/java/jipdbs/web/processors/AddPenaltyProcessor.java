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

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.CommonConstants;
import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class AddPenaltyProcessor extends FlashResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(AddPenaltyProcessor.class);
	
	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String processProcessor(ResolverContext ctx)
			throws ProcessorException {

		IDDBService app = (IDDBService) ctx.getServletContext().getAttribute("jipdbs");
		HttpServletRequest req = ctx.getRequest();
		
		String playerId = ctx.getRequest().getParameter("k");
		String type = ctx.getParameter("type");
		String redirect = ctx.getRequest().getParameter("p");
		String reason = ctx.getRequest().getParameter("reason");
		String duration = ctx.getRequest().getParameter("duration");
		String durationType = ctx.getRequest().getParameter("dt");
		
		log.debug("Redirect {}", redirect);
		
		req.setAttribute("redirect", redirect);
		
		Player player = null;
		try {
			player = app.getPlayer(playerId);
		} catch (EntityDoesNotExistsException e) {
			log.debug(e.getMessage());
			throw new HttpError(404);
		}
		
		if (!UserServiceFactory.getUserService().hasPersmission(player.getServer(), CommonConstants.ADMIN_LEVEL)) {
			Flash.error(req, MessageResource.getMessage("forbidden"));
			log.debug("Forbidden");
			return null;
		}
		
		Player currentPlayer = null;
		if (!UserServiceFactory.getUserService().getCurrentUser().isSuperAdmin()) {
			currentPlayer = UserServiceFactory.getUserService().getSubjectPlayer(player.getServer());
			if (currentPlayer == null) {
				throw new HttpError(404);
			}
		}
		
		if (StringUtils.isEmpty(reason)) {
			Flash.error(req, MessageResource.getMessage("reason_field_required"));
			return null;
		}

		Server server = null;
		try {
			server = app.getServer(player.getServer());
		} catch (EntityDoesNotExistsException e) {
			log.debug(e.getMessage());
			throw new HttpError(404);
		}
		
		Penalty penalty = new Penalty();
		penalty.setReason(reason);
		penalty.setPlayer(player.getKey());
		
		if (currentPlayer != null) penalty.setAdmin(currentPlayer.getKey());
		
		if (type.equals("notice")) {
			penalty.setType(Penalty.NOTICE);
			if ((server.getPermission() & RemotePermissions.ADD_NOTICE) == RemotePermissions.ADD_NOTICE) {
				penalty.setActive(false);
				penalty.setSynced(false);
				Flash.info(req, MessageResource.getMessage("local_action_pending"));
			} else {
				penalty.setActive(true);
				penalty.setSynced(true);
				Flash.warn(req, MessageResource.getMessage("local_action_only"));
			}
		} else {
			Long dm = Functions.time2minutes(duration + durationType);
			if (dm == 0L) {
				Flash.error(req, MessageResource.getMessage("duration_field_required"));
				return null;				
			}
			if ((server.getPermission() & RemotePermissions.ADD_BAN) == RemotePermissions.ADD_BAN) {
				penalty.setActive(false);
				penalty.setSynced(false);
				Flash.info(req, MessageResource.getMessage("local_action_pending"));
			} else {
				Flash.error(req, MessageResource.getMessage("remote_action_not_available"));
				return null;
			}
			penalty.setType(Penalty.BAN);
			penalty.setDuration(dm);
		}

		try {
			app.addPenalty(penalty, !penalty.getSynced());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
