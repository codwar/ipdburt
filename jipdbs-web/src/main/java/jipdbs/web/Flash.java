package jipdbs.web;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Flash implements Serializable {

	private static final long serialVersionUID = -1086167146182696335L;

	private static final String SESSION_NAME = Flash.class.getCanonicalName()
			+ "___flash___";

	private final List<String> infos = new LinkedList<String>();
	private final List<String> warns = new LinkedList<String>();
	private final List<String> errors = new LinkedList<String>();
	private final List<String> oks = new LinkedList<String>();

	public static void info(HttpServletRequest req, String msg) {

		Flash flash = (Flash) req.getSession().getAttribute(SESSION_NAME);
		if (flash == null) {
			flash = new Flash();
			req.getSession().setAttribute(SESSION_NAME, flash);
		}

		flash.infos.add(msg);
	}

	public static void ok(HttpServletRequest req, String msg) {

		Flash flash = (Flash) req.getSession().getAttribute(SESSION_NAME);
		if (flash == null) {
			flash = new Flash();
			req.getSession().setAttribute(SESSION_NAME, flash);
		}

		flash.oks.add(msg);
	}

	public static void warn(HttpServletRequest req, String msg) {

		Flash flash = (Flash) req.getSession().getAttribute(SESSION_NAME);
		if (flash == null) {
			flash = new Flash();
			req.getSession().setAttribute(SESSION_NAME, flash);
		}

		flash.warns.add(msg);
	}

	public static void error(HttpServletRequest req, String msg) {

		Flash flash = (Flash) req.getSession().getAttribute(SESSION_NAME);
		if (flash == null) {
			flash = new Flash();
			req.getSession().setAttribute(SESSION_NAME, flash);
		}

		flash.errors.add(msg);
	}

	public static Flash clear(HttpServletRequest req) {
		Flash flash = (Flash) req.getSession().getAttribute(SESSION_NAME);

		req.getSession().removeAttribute(SESSION_NAME);

		if (flash == null)
			return new Flash();

		return flash;
	}

	public List<String> getInfos() {
		return infos;
	}

	public List<String> getWarns() {
		return warns;
	}

	public List<String> getErrors() {
		return errors;
	}

	public List<String> getOks() {
		return oks;
	}
	
	public int getCount() {
		return infos.size() + warns.size() + errors.size() + oks.size(); 
	}
}
