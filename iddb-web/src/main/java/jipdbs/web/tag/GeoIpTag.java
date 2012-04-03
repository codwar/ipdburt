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

import iddb.util.GeoIpUtil;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Country;

public class GeoIpTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7151287922259908971L;

	private static final Logger logger = LoggerFactory.getLogger(GeoIpTag.class);
	
	private String ip;
	
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
		Country country = GeoIpUtil.getInstance().countryLookup(this.ip);
		try {
			if (country != null) {
				out.write(String.format("<img class=\"geoicon\" title=\"%s\" alt=\"[%s]\" src=\"%s/media/images/flags/%s.gif\"/>", country.getName(), country.getName(), pageContext.getServletContext().getContextPath(), country.getCode().toLowerCase()));
			} else {
				out.write("");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return EVAL_PAGE;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
