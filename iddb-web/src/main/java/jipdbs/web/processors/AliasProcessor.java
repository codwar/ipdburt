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
import iddb.info.AliasResult;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.CommonConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class AliasProcessor extends FlashResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(AliasProcessor.class);
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String key = context.getParameter("key");
		
		int page = 1;
		try {
			page = Integer.parseInt(req.getParameter("o"));
			if (page <= 0) page = 1;
		} catch (NumberFormatException e) {
		}
		
		int pageSize = CommonConstants.DEFAULT_AJAX_PAGE_LIMIT;
		int offset = (page - 1) * pageSize;

		List<AliasResult> list = new ArrayList<AliasResult>();

		int[] count = new int[1];
		if (context.hasParameter("ip")) {
			log.debug("Alias IP");
			list = app.aliasip(key, offset, pageSize, count);
		} else {
			log.debug("Alias Name");
			list = app.alias(key, offset, pageSize, count);	
		}

		req.setAttribute("hasMore", new Boolean((offset + pageSize) < count[0]));
		req.setAttribute("list", list);
		req.setAttribute("total", count[0]);
		req.setAttribute("pages", (int) Math.ceil((double) count[0] / pageSize));
		req.setAttribute("offset", page);
	
		return null;
	}

}
