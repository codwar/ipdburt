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
import iddb.core.model.Server;
import iddb.exception.EntityDoesNotExistsException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class ServerInfoProcessor extends ResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(ServerInfoProcessor.class);
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();
		
		String[] keys = req.getParameterValues("key");

		List<Server> list = new ArrayList<Server>();
		for (String key : keys) {
			try {
				Server server = app.getServer(key);
				if (server.getDirty()) {
					app.refreshServerInfo(server);
				}
				list.add(server);
			} catch (EntityDoesNotExistsException e) {
				log.error(e.getMessage());
			}
		}
		
		req.setAttribute("list", list);
	
		return null;
	}

}
