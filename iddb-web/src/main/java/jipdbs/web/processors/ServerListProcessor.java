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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class ServerListProcessor extends FlashResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(ServerListProcessor.class);
	
	public static final int SERVER_LIMIT = 50;
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {

		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		int[] count = new int[1];
		List<Server> servers = app.getServers(0, SERVER_LIMIT, count);
		
		log.info("Listing " + servers.size() + " servers");
		log.debug(">>>> COUNT " + count[0]);
		
		context.getRequest().setAttribute("servers", servers);
		context.getRequest().setAttribute("count", count[0]);
		
		return null;
	}

}
