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
import iddb.web.viewbean.PenaltyViewBean;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class PenaltyInfoProcessor extends FlashResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(PenaltyInfoProcessor.class);
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();
		
		String key = req.getParameter("key");

		Player player = null;
		PenaltyViewBean penaltyViewBean = null;
		
		try {
			player = app.getPlayer(key);
			Penalty ban = app.getLastPenalty(player);
			penaltyViewBean = new PenaltyViewBean();
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
			
		} catch (EntityDoesNotExistsException e) {
			log.error(e.getMessage());
		}
		
		req.setAttribute("key", key);
		req.setAttribute("penalty", penaltyViewBean);
	
		return null;
	}

}
