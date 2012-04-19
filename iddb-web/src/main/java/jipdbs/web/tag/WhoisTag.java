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
package jipdbs.web.tag;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoisTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7151287546548908971L;

	private static final Logger logger = LoggerFactory.getLogger(WhoisTag.class);
	
	private String ip;
	private String var;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		Writer out = pageContext.getOut();
		String value = "http://whois.domaintools.com/";
		if (this.ip == null) {
			value += "#";
		} else {
			int l = ip.lastIndexOf('.');
			if (l < 0) {
				value += "#";
			} else {
				value += ip.substring(0, l + 1) + "0";	
			}
		}
		try {
			if (this.var != null) {
				pageContext.setAttribute(this.var, value);
			} else {
				out.write(value);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return EVAL_PAGE;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
}
