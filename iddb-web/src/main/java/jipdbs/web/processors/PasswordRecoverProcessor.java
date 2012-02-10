/**
 *   Copyright(c) 2010-2012 CodWar Soft
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
import iddb.core.model.User;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.UserDAO;
import iddb.core.util.MailManager;
import iddb.core.util.PasswordUtils;
import iddb.core.util.Validator;
import iddb.exception.EntityDoesNotExistsException;
import iddb.web.security.service.UserService;
import iddb.web.security.service.UserServiceFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.Flash;
import jipdbs.web.MessageResource;
import jipdbs.web.utils.UrlUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.SimpleEntry;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.flow.ForceRedirect;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;
import ar.sgt.resolver.utils.UrlReverse;

public class PasswordRecoverProcessor extends ResponseProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PasswordRecoverProcessor.class);
	
	/* (non-Javadoc)
	 * @see jipdbs.web.processors.FlashResponseProcessor#processProcessor(ar.sgt.resolver.processor.ResolverContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doProcess(ResolverContext context)
			throws ProcessorException {
		
		HttpServletRequest req = context.getRequest();
		
		UserService userService = UserServiceFactory.getUserService();

		UrlReverse reverse = new UrlReverse(context.getServletContext());
		String home;
		try {
			home = reverse.resolve("home");
		} catch (Exception e) {
			logger.error(e.getMessage());
			home = "/";
		}

		if (userService.getCurrentUser().isAuthenticated()) {
			throw new ForceRedirect(req.getContextPath(), home);
		}
		
		UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
		IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
		
		if (context.isPost()) {
			logger.trace("POST");
			String email = req.getParameter("email");
			String passkey = req.getParameter("key");
			if (passkey != null) {
				logger.debug("Validating {}", passkey);
				String newPassword = req.getParameter("password");
				String newPasswordVer = req.getParameter("password2");
				if (!app.isRecaptchaValid(req.getRemoteAddr(),
						req.getParameter("recaptcha_challenge_field"),
						req.getParameter("recaptcha_response_field"))) {
						Flash.warn(req, MessageResource.getMessage("invalid_captcha"));
				} else if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(newPasswordVer) || StringUtils.isEmpty(email)) {
					Flash.warn(req, MessageResource.getMessage("form_required"));
				} else if (!Validator.isValidEmail(email)) {
					Flash.warn(req, MessageResource.getMessage("invalid_email"));
				} else if (!newPassword.equals(newPasswordVer)) {
					Flash.warn(req, MessageResource.getMessage("change_password_verification"));
				} else if (isValidKey(email, passkey, userDAO)) {
					try {
						User user = userDAO.get(email);
						user.setPassword(PasswordUtils.hashPassword(newPassword));
						userDAO.change_password(user);
						logger.debug("Password update done.");
						Flash.info(req, MessageResource.getMessage("change_password_success"));
						try {
							req.setAttribute("redirect", req.getContextPath() + reverse.resolve("login"));
						} catch (Exception e) {
							logger.error(e.getMessage());
							req.setAttribute("redirect", req.getContextPath() + home);
						}
						return "/include/redirect.jsp";
					} catch (EntityDoesNotExistsException e) {
						Flash.warn(req, MessageResource.getMessage("invalid_user"));
					}
					return "/page/message.jsp";
				}  else {
					Flash.error(req, MessageResource.getMessage("invalid_password_key"));
					return "/page/message.jsp";
				}
				req.setAttribute("key", passkey);
				return "/page/password_recover_step2.jsp";
			} else {
				logger.debug("Requesting new key");
				if (!app.isRecaptchaValid(req.getRemoteAddr(),
						req.getParameter("recaptcha_challenge_field"),
						req.getParameter("recaptcha_response_field"))) {
						Flash.warn(req, MessageResource.getMessage("invalid_captcha"));
				} else if (StringUtils.isEmpty(email)) {
					Flash.warn(req, MessageResource.getMessage("form_required"));
				} else {
					try {
						userDAO.get(email);
					} catch (EntityDoesNotExistsException e1) {
						Flash.warn(req, MessageResource.getMessage("invalid_user"));
						return null;
					}
					String s = userService.generatePassKey(email);
					userDAO.savePassKey(email, s);
					Map<String, String> d = new HashMap<String, String>();
					String link;
					try {
						link = reverse.resolve("passwordrecovery_link", new Entry[]{new SimpleEntry("key", s)});
					} catch (Exception e) {
						logger.error(e.getMessage());
						throw new ProcessorException(e);
					}
					d.put("link", UrlUtils.getRealPath(req, link));
					try {
						MailManager.getInstance().sendMail(MessageResource.getMessage("mail_password_recover_subject"),
															MessageResource.getMessage("mail_password_recover_template"),
															new String[]{email}, d);
						Flash.info(req, MessageResource.getMessage("password_recover_mail_sent"));
					} catch (Exception e) {
						Flash.error(req, e.getMessage());
					}
					return "/page/message.jsp"; 
				}
			}
		} else {
			logger.trace("GET");
			String passkey = context.getParameter("key");
			if (passkey != null) {
				if (isValidKey(passkey, userDAO)) {
					req.setAttribute("key", passkey);
				}  else {
					Flash.error(req, MessageResource.getMessage("invalid_password_key"));
					return "/page/message.jsp";
				}
				return "/page/password_recover_step2.jsp";
			}
		}
		return null;
	}

	/**
	 * @param email
	 * @param passkey
	 * @param userDAO2 
	 * @return
	 */
	private boolean isValidKey(String email, String passkey, UserDAO userDAO) {
		return email.equals(userDAO.findPassKey(passkey, 24));
	}

	private boolean isValidKey(String passkey, UserDAO userDAO) {
		return userDAO.findPassKey(passkey, 24) != null;
	}
	
}
