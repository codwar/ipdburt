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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import iddb.core.IDDBService;
import iddb.core.model.Server;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class AdvancedSearchProcessor extends ResponseProcessor {

	/* (non-Javadoc)
	 * @see ar.sgt.resolver.processor.ResponseProcessor#doProcess(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String doProcess(ResolverContext ctx) throws ProcessorException {

		HttpServletRequest req = ctx.getRequest();

		IDDBService app = (IDDBService) ctx.getServletContext().getAttribute("jipdbs");

		List<Server> servers = app.getActiveServers();
		
		Collections.sort(servers, new Comparator<Server>() {
			@Override
			public int compare(Server o1, Server o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		req.setAttribute("servers", servers);
		
		return null;
	}

}
