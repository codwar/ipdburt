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
import iddb.core.util.GuidGenerator;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.Flash;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.RedirectProcessor;
import ar.sgt.resolver.processor.ResolverContext;

public class SaveServerProcessor extends RedirectProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String name = req.getParameter("name");
		String admin = req.getParameter("admin");
		String ip = req.getParameter("ip");

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(admin)) {
			Flash.error(req, "Falta nombre o admin del server.");
			return null;
		}

		if (StringUtils.isEmpty(ip))
			Flash.warn(req, "No se indicó dirección IP. No se realizará comprobación del origen de los datos.");

		if (StringUtils.isEmpty(req.getParameter("k"))) {
			String uid = GuidGenerator.generate(name);
			app.addServer(name, admin, uid, ip);
			Flash.info(req, "Server agregado.");
		} else {
			app.saveServer(req.getParameter("k"), name, admin, ip);
			Flash.info(req, "Server editado.");
		}
		
		return null;
	}

}
