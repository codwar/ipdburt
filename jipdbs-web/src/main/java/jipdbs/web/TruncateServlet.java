package jipdbs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.model.dao.PlayerDAO;
import jipdbs.core.model.dao.impl.AliasDAOImpl;
import jipdbs.core.model.dao.impl.PlayerDAOImpl;
import jipdbs.core.util.LocalCache;

public class TruncateServlet extends HttpServlet {

	private static final long serialVersionUID = -8523957912890704182L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PlayerDAO playerDAO = new PlayerDAOImpl();
		AliasDAO aliasDAO = new AliasDAOImpl();
		
		aliasDAO.truncate();
		playerDAO.truncate();
		
		LocalCache.getInstance().clear();
		
		resp.getWriter().write("Done");
		
	}
}
