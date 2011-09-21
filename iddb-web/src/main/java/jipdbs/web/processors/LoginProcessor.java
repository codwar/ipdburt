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

import iddb.core.security.User;
import iddb.core.security.UserService;
import iddb.core.security.UserServiceFactory;
import iddb.core.security.exceptions.InvalidAccountException;
import iddb.core.security.exceptions.InvalidCredentialsException;
import iddb.core.security.exceptions.UserLockedException;
import jipdbs.web.Flash;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.flow.ForceRedirect;
import ar.sgt.resolver.processor.ResolverContext;

public class LoginProcessor extends FlashResponseProcessor {

	//private static final Logger log = LoggerFactory.getLogger(LoginProcessor.class);
	
	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public String processProcessor(ResolverContext context)
			throws ProcessorException {
		UserService userService = UserServiceFactory.getUserService();
		if ("logout".equals(context.getParameter("type"))) {
			userService.logout();
			throw new ForceRedirect(context.getRequest().getContextPath());
		} else {
			User user = userService.getCurrentUser();
			String next = StringUtils.isEmpty(context.getRequest().getParameter("next")) ? context.getRequest().getContextPath() : context.getRequest().getParameter("next");
			if (user.isAuthenticated()) return next;
			if (context.isPost()) {
				try {
					userService.authenticate(context.getRequest().getParameter("username"), context.getRequest().getParameter("password"));
					throw new ForceRedirect(next);
				} catch (InvalidAccountException e) {
					Flash.error(context.getRequest(), "Usuario o contraseña no válidos.");
				} catch (InvalidCredentialsException e) {
					Flash.error(context.getRequest(), "Usuario o contraseña no válidos.");
				} catch (UserLockedException e) {
					Flash.error(context.getRequest(), "Su cuenta se encuentra bloqueada.");
				}
			}
			context.getRequest().setAttribute("next", next);
		}
		return null;
	}

}
