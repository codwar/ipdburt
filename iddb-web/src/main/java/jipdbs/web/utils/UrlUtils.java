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
package jipdbs.web.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 12072245
 *
 */
public final class UrlUtils {

	public static String getRealPath(HttpServletRequest req, String url) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(req.getScheme()).append("://");
		buffer.append(req.getServerName());
		if (req.getServerPort() != 80) {
			buffer.append(":").append(req.getServerPort());
		}
		buffer.append(req.getContextPath());
		buffer.append(url);
		return buffer.toString();
	}
}
