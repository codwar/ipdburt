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
package jipdbs.web.tag;

import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class GravatarTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316911590160088824L;
	
	private String email;
	private String size;
	private String type;
	private String claz;
	
	private static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
	private static final String DEFAULT_SIZE = "32";
	private static final String DEFAULT_TYPE = "wavatar";
	
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
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			throw new JspException(e.getMessage());
		}
		md.update(getEmail().toLowerCase().getBytes());
		byte[] digestBytes = md.digest();
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digestBytes.length; i++) {
          sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
		String emailhash = sb.toString();
		String type = getType() == null ? DEFAULT_TYPE : getType();
		String size = getSize() == null ? DEFAULT_SIZE : getSize();
		String url = GRAVATAR_URL + emailhash + "/?s=" + size + "&d=" + type;

		StringBuilder img = new StringBuilder("<img");
		if (getClaz() != null) img.append(" class=\"").append(getClaz()).append("\"");
		img.append(" border=\"0\" align=\"top\" src=\"").append(url).append("\" with=\"").append(size).append("px\" />");
		try {
			out.write(img.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClaz() {
		return claz;
	}

	public void setClaz(String claz) {
		this.claz = claz;
	}
}
