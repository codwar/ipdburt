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
