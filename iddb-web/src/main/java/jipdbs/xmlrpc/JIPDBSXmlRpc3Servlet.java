package jipdbs.xmlrpc;

import iddb.core.JIPDBS;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.xmlrpc.handler.JIPDBSRpc3Handler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

public class JIPDBSXmlRpc3Servlet extends XmlRpcServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -472531421906415406L;

	private static ThreadLocal<String> clientIpAddress = new ThreadLocal<String>();
	
	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");

	}

	/**
	 * Get Client IP Address
	 */
	public static String getClientIpAddress() {
		return (String) clientIpAddress.get();
	}
	
	@Override
	public void doPost(HttpServletRequest pRequest,
			HttpServletResponse pResponse) throws IOException, ServletException {
		clientIpAddress.set(pRequest.getRemoteAddr());
		super.doPost(pRequest, pResponse);
	}
	
	@Override
	protected XmlRpcHandlerMapping newXmlRpcHandlerMapping()
			throws XmlRpcException {
		return new InstanceHandlerMapping(new JIPDBSRpc3Handler(app));
	}

}
