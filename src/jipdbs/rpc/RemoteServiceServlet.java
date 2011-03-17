package jipdbs.rpc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.rpc.handlers.InfoUpdater;
import redstone.xmlrpc.XmlRpcServlet;

@SuppressWarnings("serial")
public class RemoteServiceServlet extends XmlRpcServlet {

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		getXmlRpcServer().addInvocationHandler(null, new InfoUpdater());
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello there!");
	}
}
