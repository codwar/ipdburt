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
package jipdbs.http;

import iddb.core.JIPDBS;
import iddb.core.model.Server;
import iddb.exception.EntityDoesNotExistsException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JIPDBSHTTPHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 581879616460472029L;
	private static final Logger log = LoggerFactory.getLogger(JIPDBSHTTPHandler.class);

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		this.app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if ("/fetchserver".equalsIgnoreCase(req.getPathInfo())) {
			resp.setContentType("text/plain");
			String key = req.getParameter("key");
			Server server = null;
			try {
				server = app.getServer(key);
			} catch (EntityDoesNotExistsException e) {
				log.error(e.getMessage());
			}
			if (server == null) {
				resp.getWriter().println(
						"{\"error\": true, \"server\": {\"key\": \"" + key
								+ "\"}}");
			} else {
				if (server.getDirty()) {
					app.refreshServerInfo(server);
				}
				resp.getWriter().println(
						"{\"error\": false, \"server\": {\"key\": \""
								+ server.getKey() + "\", \"count\": \""
								+ server.getOnlinePlayers() + "\",\"name\": \""
								+ server.getName() + "\"}}");
			}
		}
	}
}
