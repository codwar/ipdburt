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

import iddb.core.ApplicationError;
import iddb.core.IDDBService;
import iddb.core.model.Server;
import iddb.core.util.HashUtils;
import iddb.exception.EntityDoesNotExistsException;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.RedirectProcessor;
import ar.sgt.resolver.processor.ResolverContext;

public class SaveServerProcessor extends RedirectProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String name = req.getParameter("name");
		String admin = req.getParameter("admin");
		String ip = req.getParameter("ip");
		String disabled = req.getParameter("disable") == null ? "off" : req.getParameter("disable");
		String displayip = req.getParameter("displayip");
		
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(admin)) {
			Flash.error(req, MessageResource.getMessage("save_server_noname"));
			return null;
		}
		
		if (StringUtils.isEmpty(ip))
			Flash.warn(req, MessageResource.getMessage("save_server_noip"));

		try {
			if (StringUtils.isEmpty(req.getParameter("k"))) {
				String uid = HashUtils.generate(name);
				Server server = app.addServer(name, admin, uid, ip, disabled.equalsIgnoreCase("on"));
				server.setDisplayAddress(displayip);
				app.saveServer(server);
				Flash.info(req, MessageResource.getMessage("save_server_added"));
			} else {
				Server server;
				try {
					server = app.getServer(req.getParameter("k"));
				} catch (EntityDoesNotExistsException e) {
					Flash.error(req, MessageResource.getMessage("server_not_found"));
					return null;
				}
				server.setName(name);
				server.setAdminEmail(admin);
				server.setAddress(ip);
				server.setDisplayAddress(displayip);
				server.setDisabled(disabled.equalsIgnoreCase("on"));
				app.saveServer(server);
				Flash.info(req, MessageResource.getMessage("save_server_updated"));
			}
		} catch (ApplicationError e) {
			Flash.error(req, e.getMessage());
		}
		
		return null;
	}

}
