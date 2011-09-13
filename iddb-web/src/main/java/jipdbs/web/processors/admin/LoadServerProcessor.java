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
package jipdbs.web.processors.admin;

import iddb.core.JIPDBS;
import iddb.core.model.Server;
import iddb.exception.EntityDoesNotExistsException;
import jipdbs.web.processors.ServerListProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class LoadServerProcessor extends ServerListProcessor {

	private static final Logger log = LoggerFactory.getLogger(LoadServerProcessor.class);
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		String resp = super.doProcess(context);
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");

		Server server = null;
		try {
			server = app.getServer(context.getParameter("key"));
		} catch (EntityDoesNotExistsException e) {
			log.error(e.getMessage());
		}
		context.getRequest().setAttribute("server", server);	
		
		return resp;
	}

}
