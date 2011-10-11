package jipdbs.xmlrpc;

import iddb.core.IDDBService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.xmlrpc.handler.RPC3Handler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

public class JIPDBSXmlRpc3Servlet extends XmlRpcServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -472531421906415406L;

	private static ThreadLocal<String> clientIpAddress = new ThreadLocal<String>();
	
	private IDDBService app;

	@Override
	public void init() throws ServletException {
		super.init();
		app = (IDDBService) getServletContext().getAttribute("jipdbs");
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
		return new InstanceHandlerMapping(new RPC3Handler(app));
	}

}
