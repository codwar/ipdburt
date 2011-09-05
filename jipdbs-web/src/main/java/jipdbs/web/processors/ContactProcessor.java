package jipdbs.web.processors;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import jipdbs.core.JIPDBS;
import jipdbs.core.util.Validator;
import jipdbs.web.Flash;

import org.datanucleus.util.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class ContactProcessor extends FlashResponseProcessor {

	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {

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
			
			JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
			
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
