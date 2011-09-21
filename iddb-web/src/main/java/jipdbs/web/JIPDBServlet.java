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

import iddb.core.IDDBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public abstract class JIPDBServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7720689580181419628L;
	
	protected IDDBService app;
	
	@Override
	public void init() throws ServletException {
		app = (IDDBService) getServletContext().getAttribute(Context.JIPDBS);
	}
	
}
