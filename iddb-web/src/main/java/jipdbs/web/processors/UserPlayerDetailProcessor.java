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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iddb.core.IDDBService;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.UserServer;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.UserServerDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.viewbean.UserPlayerDetailViewBean;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class UserPlayerDetailProcessor extends ResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(UserPlayerDetailProcessor.class);
	
	/* (non-Javadoc)
	 * @see ar.sgt.resolver.processor.ResponseProcessor#doProcess(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String doProcess(ResolverContext ctx) throws ProcessorException {

		IDDBService app = (IDDBService) ctx.getServletContext().getAttribute("jipdbs");
		
		UserServerDAO dao = (UserServerDAO) DAOFactory.forClass(UserServerDAO.class);
		
		List<UserServer> userServers = dao.findByUser(UserServiceFactory.getUserService().getCurrentUser().getKey());
		
		List<UserPlayerDetailViewBean> list = new ArrayList<UserPlayerDetailViewBean>();
		
		for (UserServer userServer : userServers) {
			try {
				Player player = app.getPlayer(userServer.getPlayer());
				Server server = app.getServer(userServer.getServer());
				UserPlayerDetailViewBean b = new UserPlayerDetailViewBean();
				b.setId(userServer.getKey());
				b.setPlayer(player);
				b.setServer(server);
				list.add(b);
			} catch (EntityDoesNotExistsException e) {
				log.error(e.getMessage());
			}
		}

		Collections.sort(list, new Comparator<UserPlayerDetailViewBean>() {
			@Override
			public int compare(UserPlayerDetailViewBean o1,
					UserPlayerDetailViewBean o2) {
				return o1.getServer().getName().compareTo(o2.getServer().getName());
			}
		});
		
		ctx.getRequest().setAttribute("list", list);
		
		return null;
	}

}
