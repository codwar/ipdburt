package jipdbs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.data.AliasDAO;
import jipdbs.data.PlayerDAO;
import jipdbs.util.LocalCache;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class TruncateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8523957912890704182L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PlayerDAO playerDAO = new PlayerDAO();
		AliasDAO aliasDAO = new AliasDAO();
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		
		aliasDAO.truncate(service);
		playerDAO.truncate(service);
		
		LocalCache.getInstance().clear();
		
		resp.getWriter().write("Done");
		
	}
}
