package jipdbs.web;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.JIPDBS;

import org.datanucleus.util.StringUtils;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {

	private final String FROM_ADDR = "contact@ipdburt.appspotmail.com";

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/contact.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		String mail = req.getParameter("m");
		String text = req.getParameter("text");

		if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(text))
			resp.sendRedirect("/contact.jsp?e=1");

		if (!app.isRecaptchaValid(req.getRemoteAddr(),
				req.getParameter("recaptcha_challenge_field"),
				req.getParameter("recaptcha_response_field")))
			resp.sendRedirect("/contact.jsp?e=1");

		StringBuilder builder = new StringBuilder();
		builder.append("Responder a: ");
		builder.append(mail);
		builder.append("\r\n");
		if (user != null) {
			builder.append("Identificado como: ");
			builder.append(user.getEmail());
			builder.append("\r\n");
		}
		builder.append("\r\n");
		builder.append("------------- MENSAJE -------------\r\n\r\n");
		builder.append(text);

		sendMail(mail, builder.toString());

		resp.sendRedirect("/contact.jsp?m=1");

	}

	private void sendMail(String mail, String text) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Address[] replyTo = new InternetAddress[1];
			replyTo[0] = new InternetAddress(mail);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM_ADDR));
			msg.setReplyTo(replyTo);
			msg.addRecipient(RecipientType.TO, new InternetAddress("admins"));
			msg.setSubject("Mensaje enviado desde IPDB");
			msg.setText(text);
			Transport.send(msg);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
