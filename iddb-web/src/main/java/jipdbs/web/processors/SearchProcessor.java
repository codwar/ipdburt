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
import iddb.core.PageLink;
import iddb.core.Parameters;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.util.Functions;
import iddb.core.util.Validator;
import iddb.exception.EntityDoesNotExistsException;
import iddb.info.SearchResult;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.viewbean.PenaltyViewBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.CommonConstants;
import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class SearchProcessor extends FlashResponseProcessor {

	/**
	 * 
	 */
	private static final int MIN_LENGTH_QUERY = 4;
	private static final Logger log = LoggerFactory.getLogger(SearchProcessor.class);

	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {

		HttpServletRequest req = context.getRequest();

		IDDBService app = (IDDBService) context.getServletContext()
				.getAttribute("jipdbs");

		int page = 1;
		int pageSize = CommonConstants.DEFAULT_PAGE_SIZE;
		String query = null;
		String type = null;
		String mode = null;
		String match = null;
		
		if (context.getParameter("query") != null) {
			query = context.getParameter("query");
		}
		if (context.getParameter("type") != null) {
			type = context.getParameter("type");
		}
		if (req.getParameter("mode") != null) {
			mode = req.getParameter("mode");
		}
		if (req.getParameter("p") != null) {
			try {
				page = Integer.parseInt(req.getParameter("p"));
			} catch (NumberFormatException e) {
			}
		}
		if (req.getParameter("ps") != null) {
			try {
				pageSize = Integer.parseInt(req.getParameter("ps"));
			} catch (NumberFormatException e) {
			}
		}
		match = req.getParameter("match");
		
		int offset = (page - 1) * pageSize;
		int limit = Math.min(pageSize, Parameters.MAX_ENTITY_LIMIT);
		int[] total = new int[1];

		String queryValue = query;

		long time = System.currentTimeMillis();

		List<SearchResult> list = new ArrayList<SearchResult>();

		if ("server".equals(type)) {
			log.debug("Server filter");
			queryValue = "";
			try {
				list = app.byServerSearch(Long.parseLong(query), offset, limit, total);
			} catch (NumberFormatException e) {
				log.error("Invalid server id: {}", query);
			}
		} else if ("ban".equals(type)) {
			log.debug("Ban list");
			list = app.bannedQuery(offset, limit, total);
		} else if (StringUtils.isEmpty(query)) {
			log.debug("Root");
			list = app.rootQuery(offset, limit, total);
		} else if ("adv".equals(type)) {
			String server = req.getParameter("server");
			log.debug("Advanced search - query: {} - server: {}", query, server);
			list = app.aliasAdvSearch(query, server, offset, limit, total);
			if (total[0] > Parameters.MAX_SEARCH_LIMIT) {
				Flash.warn(req,MessageResource.getMessage("too_many_results"));
			}
		} else if (Validator.isValidSearchIp(query)) {
			log.debug("Search IP {}", query);
			query = Functions.fixIp(query);
			queryValue = query;
			list = app.ipSearch(query, offset, limit, total);
		} else if (Validator.isValidClientId(query)) {
			log.debug("Search Client ID {}", query);
			try {
				Long clientId = Long.parseLong(query.substring(1));
				list = app.clientIdSearch(clientId, offset, limit, total);
			} catch (NumberFormatException e) {
				log.error("Invalid client id: {}", query);
			}
		} else {
			log.debug("Search Alias " + query);
			if (query.length() >= MIN_LENGTH_QUERY && Validator.isValidPlayerName(query)) {
				list = app.aliasSearch(query, offset, limit, total, "exact".equals(match));
			} else {
				log.debug("Using advanced mode");
				if (query.length() < MIN_LENGTH_QUERY) {
					query = query + "*";
				}
				if (!query.startsWith("+") && !query.startsWith("-")) {
					query = "+" + query;
				}
				list = app.aliasAdvSearch(query, null, offset, limit, total);
			}
			if (total[0] > Parameters.MAX_SEARCH_LIMIT) {
				Flash.warn(req,MessageResource.getMessage("too_many_results"));
			}
		}

		time = System.currentTimeMillis() - time;

		int totalElements = Math.min(Parameters.MAX_ENTITY_LIMIT, total[0]);

		int totalPages = (int) Math.ceil((double) totalElements / pageSize);

		Boolean hasAdmin = UserServiceFactory.getUserService().hasAnyServer(CommonConstants.ADMIN_LEVEL); 
		if (hasAdmin) {
			for (SearchResult result : list) {
				if (result.isBanned()) {
					Penalty ban = app.getLastPenalty(result.getKey());
					if (ban != null) {
						PenaltyViewBean penaltyViewBean = new PenaltyViewBean();
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
						result.setBaninfo(penaltyViewBean.toString());
					}					
				}
			}
		}
		req.setAttribute("list", list);
		req.setAttribute("count", totalElements);
		req.setAttribute("time", time);
		req.setAttribute("queryValue", queryValue);
		req.setAttribute("query", query);
		req.setAttribute("pageLink", new PageLink(page, pageSize, totalPages));

		if ("code".equals(mode)) return "/data/search_bbcode.jsp";
		return null;
	}

}
