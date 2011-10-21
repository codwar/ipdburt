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
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.service.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.CommonConstants;
import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.flow.ForceRedirect;
import ar.sgt.resolver.processor.ResolverContext;

/**
 * @author 12072245
 *
 */
public class AddPenaltyProcessor extends FlashResponseProcessor {

	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String processProcessor(ResolverContext ctx)
			throws ProcessorException {

		IDDBService app = (IDDBService) ctx.getServletContext().getAttribute("jipdbs");
		
		String playerId = ctx.getRequest().getParameter("k");

		Player player = null;
		try {
			player = app.getPlayer(playerId);
		} catch (EntityDoesNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!UserServiceFactory.getUserService().hasPersmission(player.getServer(), CommonConstants.ADMIN_LEVEL)) {
			// TODO forbiden
		}
		
		Player currentPlayer = UserServiceFactory.getUserService().getSubjectPlayer(player.getServer());
		if (currentPlayer == null) {
			// THIS CANT HAPPEN
		}
		
		String type = ctx.getParameter("type");
		String redirect = ctx.getRequest().getParameter("p");
		String reason = ctx.getRequest().getParameter("reason");
		HttpServletRequest req = ctx.getRequest();
		
		if (StringUtils.isEmpty(reason)) {
			Flash.error(req, MessageResource.getMessage("reason_field_required"));
			throw new ForceRedirect(redirect);
		}
		
		Penalty penalty = new Penalty();
		penalty.setReason(reason);
		penalty.setAdmin(currentPlayer.getKey());
		
		// TODO CHECK SERVER PERMISSIONS
		if (type.equals("notice")) {
			
		}
		
		return null;
		
	}

}
