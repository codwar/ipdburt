package jipdbs.xmlrpc;

import iddb.exception.UpdateApiException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceHandlerMapping implements XmlRpcHandlerMapping {

	private final Object instance;
	private static final Logger log = LoggerFactory.getLogger(InstanceHandlerMapping.class);

	public InstanceHandlerMapping(Object instance) {
		this.instance = instance;
	}

	@Override
	public XmlRpcHandler getHandler(String methodName)
			throws XmlRpcNoSuchHandlerException, XmlRpcException {

		return new XmlRpcHandler() {

			@Override
			public Object execute(XmlRpcRequest req) throws XmlRpcException {

				try {
					/*
					 * For backward compatibility. Strip out namespace
					 */
					String[] methods = req.getMethodName().split("\\.");
					String methodName = methods[methods.length - 1];

					Class<?>[] types = new Class<?>[req.getParameterCount()];
					Object[] args = new Object[req.getParameterCount()];

					for (int i = 0; i < args.length; i++) {
						Object param = req.getParameter(i);
						args[i] = param;
						types[i] = param.getClass();
					}

					Method method = instance.getClass().getMethod(methodName,
							types);
					Object result = method.invoke(instance, args);

					return result != null ? result : "";
				} catch (Exception e) {
					if (!(e instanceof UpdateApiException)) {
						log.error(e.getMessage());
						StringWriter w = new StringWriter();
						e.printStackTrace(new PrintWriter(w));
						log.error(w.getBuffer().toString());					
					}
					throw new XmlRpcException(e.getMessage());
				}
			}
		};
	}
}
