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
import iddb.core.util.Validator;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.Flash;

import org.apache.commons.lang.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class ContactProcessor extends ResponseProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {

		if (context.isPost()) {
			HttpServletRequest req = context.getRequest();
			String mail = req.getParameter("m");
			String text = req.getParameter("text");

			if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(text)) {
				Flash.error(req, "Completa todos los campos solicitados.");
				return null;
			}
			
			if (!Validator.isValidEmail(mail)) {
				Flash.error(req, "Ingresa una dirección de correo válida.");
				return null;
			}
			
			IDDBService app = (IDDBService) context.getServletContext().getAttribute("jipdbs");
			
			if (!app.isRecaptchaValid(req.getRemoteAddr(),
					req.getParameter("recaptcha_challenge_field"),
					req.getParameter("recaptcha_response_field"))) {
					Flash.error(req, "Código no válido.");
					return null;
				
			}
			Principal user = req.getUserPrincipal();
			app.sendAdminMail(user != null ? user.getName() : null, mail, text);
			Flash.ok(req, "Tu mensaje fue enviado.");
		}
		return null;
	}

}
