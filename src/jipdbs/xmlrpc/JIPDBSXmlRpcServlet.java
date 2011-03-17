package jipdbs.xmlrpc;

import javax.servlet.ServletException;

import jipdbs.JIPDBS;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

public class JIPDBSXmlRpcServlet extends XmlRpcServlet {

	private static final long serialVersionUID = -3633984619886267577L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");

	}

	@Override
	protected XmlRpcHandlerMapping newXmlRpcHandlerMapping()
			throws XmlRpcException {

		return new InstanceHandlerMapping(new JIPDBSRpcHandler(app));
	}

}
