package jipdbs.web;

import iddb.core.JIPDBS;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public abstract class JIPDBServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7720689580181419628L;
	
	protected JIPDBS app;
	
	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute(Context.JIPDBS);
	}
	
}
