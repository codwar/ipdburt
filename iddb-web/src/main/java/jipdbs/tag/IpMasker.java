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
package jipdbs.tag;

import iddb.core.util.Functions;
import iddb.web.security.service.UserService;
import iddb.web.security.service.UserServiceFactory;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jipdbs.web.CommonConstants;

public class IpMasker extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382221808978170877L;
	
	private String value;
	private String var;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			if (value == null || value.equals("")) {
				out.write("-");
			} else {
				String h;
				UserService userService = UserServiceFactory.getUserService();
				if (userService.hasAnyServer(CommonConstants.ADMIN_LEVEL)) {
					h = value;
				} else {
					h = Functions.maskIpAddress(value);
				}
				if (var != null && !var.equals("")) {
					pageContext.setAttribute(var, h);
				} else {
					out.write(h);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
}
