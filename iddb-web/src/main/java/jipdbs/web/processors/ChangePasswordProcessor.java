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

import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.UserDAO;
import iddb.core.util.PasswordUtils;
import iddb.web.security.service.UserService;
import iddb.web.security.service.UserServiceFactory;
import iddb.web.security.subject.Subject;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.Flash;
import jipdbs.web.MessageResource;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class ChangePasswordProcessor extends ResponseProcessor {

	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String doProcess(ResolverContext context)
			throws ProcessorException {
		
		if (context.isPost()) {
			HttpServletRequest req = context.getRequest();
			String currentPassword = req.getParameter("current");
			String newPassword = req.getParameter("password");
			String newPasswordVer = req.getParameter("password2");
			if (StringUtils.isEmpty(currentPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(newPasswordVer)) {
				Flash.warn(req, MessageResource.getMessage("form_required"));
			} else if (!newPassword.equals(newPasswordVer)) {
				Flash.warn(req, MessageResource.getMessage("change_password_verification"));
			} else {
				UserService userService = UserServiceFactory.getUserService();
				Subject subject = userService.getCurrentUser();
				if (PasswordUtils.checkPassword(currentPassword, subject.getPassword())) {
					UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
					subject.setPassword(PasswordUtils.hashPassword(newPassword));
					userDAO.change_password(subject);
					Flash.info(req, MessageResource.getMessage("change_password_success"));
				} else {
					Flash.warn(req, MessageResource.getMessage("change_password_invalid"));
				}
			}
		}
		return null;
	}

}
