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

import iddb.core.util.Validator;

import javax.servlet.jsp.JspTagException;

import org.apache.taglibs.standard.tag.common.core.WhenTagSupport;

public class WhenValidGuidTag extends WhenTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382221808978170877L;
	
	private String test;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport#condition()
	 */
	@Override
	protected boolean condition() throws JspTagException {
		return Validator.isValidGuid(test);
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
}
