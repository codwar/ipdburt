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
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.User;
import iddb.core.model.UserServer;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.UserDAO;
import iddb.core.model.dao.UserServerDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.viewbean.UserServerViewBean;
import iddb.web.viewbean.UserViewBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.CommonConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class UserAdminProcessor extends ResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(UserAdminProcessor.class);
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		if (!UserServiceFactory.getUserService().getCurrentUser().isSuperAdmin()) {
			throw new HttpError(HttpServletResponse.SC_FORBIDDEN);
		}
		
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		int page = 1;
		int pageSize = CommonConstants.DEFAULT_PAGE_SIZE;

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
		
		int offset = (page - 1) * pageSize;
		int[] total = new int[1];
		
		UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
		UserServerDAO userServerDAO = (UserServerDAO) DAOFactory.forClass(UserServerDAO.class);
		
		List<User> users = userDAO.findAll(offset, pageSize, total);
			
		List<UserViewBean> list = new ArrayList<UserViewBean>();
		
		for (User user : users) {
			List<UserServer> servers = userServerDAO.findByUser(user.getKey());
			UserViewBean viewBean = new UserViewBean();
			viewBean.setKey(user.getKey());
			viewBean.setUsername(user.getLoginId());
			for (UserServer us : servers) {
				try {
					Player player = app.getPlayer(us.getPlayer());
					Server server = app.getServer(us.getServer());
					viewBean.getList().add(new UserServerViewBean(server, player));
				} catch (EntityDoesNotExistsException e) {
					log.warn(e.getMessage());
				} catch (ApplicationError e) {
					log.error(e.getMessage());
					throw new ProcessorException(e);
				}
			}
			list.add(viewBean);
		}

		context.getRequest().setAttribute("users", list);	
		
		return null;
	}

}
