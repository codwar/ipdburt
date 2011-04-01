package jipdbs.web;

import java.io.IOException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;

import org.datanucleus.util.StringUtils;

@SuppressWarnings("serial")
public class ContactPostServlet extends HttpServlet {

	private JIPDBS app;
	private final static String MAIL_RE = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
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
		
		Pattern mailre = Pattern.compile(MAIL_RE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = mailre.matcher(mail);
		
		if (!matcher.matches()) {
			Flash.error(req, "Ingresa una dirección de correo válida.");
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
	
	public static void main(String[] args) {
		Pattern mailre = Pattern.compile(MAIL_RE);
		System.out.println(mailre.matcher("mail@mail.com").matches());
		System.out.println(mailre.matcher("mail@mail.com.ar").matches());
		System.out.println(mailre.matcher("mail.mail@mail.com.ar").matches());
		System.out.println(mailre.matcher("mail.mail_mail.com.ar").matches());
	}
}
