package jipdbs.web;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;

import org.datanucleus.util.StringUtils;

@SuppressWarnings("serial")
public class ContactPostServlet extends HttpServlet {

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String mail = req.getParameter("m");
		String text = req.getParameter("text");

		if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(text)) {
			Flash.error(req, "Completa todos los campos solicitados.");
			return;
		}

		if (!app.isRecaptchaValid(req.getRemoteAddr(),
				req.getParameter("recaptcha_challenge_field"),
				req.getParameter("recaptcha_response_field"))) {
				Flash.error(req, "Alguno de los datos es inválido.");
				return;
			
		}
		
		Principal user = req.getUserPrincipal();
		
		app.sendAdminMail(user != null ? user.getName() : null, mail, text);
		
		Flash.ok(req, "Tu mensaje fue enviado.");
	}
}
