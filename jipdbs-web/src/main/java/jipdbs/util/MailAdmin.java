package jipdbs.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailAdmin {

	private static final String FROM_ADDR = "sys@ipdburt.appspotmail.com";
	private static final Logger log = Logger.getLogger(MailAdmin.class.getName());
	
	public static void sendMail(String subject, String message) {
		try {
			Session session = Session
					.getDefaultInstance(new Properties(), null);
			
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM_ADDR));
			msg.addRecipient(RecipientType.TO, new InternetAddress("admins"));
			msg.setSubject("IPDB: " + subject);
			msg.setText(message);
			Transport.send(msg);
		} catch (AddressException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		} catch (MessagingException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

}
