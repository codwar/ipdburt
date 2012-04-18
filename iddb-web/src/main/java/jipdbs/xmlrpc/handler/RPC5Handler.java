package jipdbs.xmlrpc.handler;

import iddb.api.ServerManager;
import iddb.core.IDDBService;
import iddb.core.model.Server;
import iddb.exception.UnauthorizedUpdateException;
import iddb.exception.UpdateApiException;

import java.util.Arrays;
import java.util.List;

import jipdbs.xmlrpc.JIPDBSXmlRpc5Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPC5Handler extends RPC4Handler {

	private static final Logger log = LoggerFactory.getLogger(RPC5Handler.class);
	
	/**
	 * @param app
	 */
	public RPC5Handler(IDDBService app) {
		super(app);
	}

	public void updateName(String key, String name, Object[] data) throws UpdateApiException {
		if (log.isDebugEnabled()) {
			log.debug("Update Server {} - {}", name, Arrays.toString(data));	
		}
		try {
			this.updateApi.updateName(key, name, (String) data[0], (Integer) data[1], getClientAddress(), smartCast(data[2]), parseLong(data[3]).intValue());			
		} catch (Exception e) {
			throw new UpdateApiException(e.getClass().getName());
		}
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.xmlrpc.handler.RPC3Handler#getClientAddress()
	 */
	@Override
	public String getClientAddress() {
		return JIPDBSXmlRpc5Servlet.getClientIpAddress();
	}

	/* (non-Javadoc)
	 * @see jipdbs.xmlrpc.handler.RPC3Handler#getAuthorizedServer(java.lang.String)
	 */
	public Server getAuthorizedServer(String key, String pubIp, Integer port)
			throws UnauthorizedUpdateException {
		return ServerManager.getAuthorizedServer(key, getClientAddress(), null, pubIp, port);
	}
	/**
	 * 
	 * @param key
	 * @param plist
	 * @param timestamp
	 * @throws UpdateApiException
	 * @throws Exception
	 */
	public void update(String key, Object[] plist, Integer timestamp, Object[] data) throws UpdateApiException {
		Server server = null;
		try {
			server = getAuthorizedServer(key, smartCast(data[0]), parseLong(data[1]).intValue());
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
			throw new UpdateApiException("UnauthorizedUpdateException");
		}
		update(server, plist, timestamp);
	}

	public Integer register(String key, String userid, Object[] data, Object[] newData) throws UpdateApiException {
		Server server = null;
		try {
			server = getAuthorizedServer(key, smartCast(newData[0]), parseLong(newData[1]).intValue());
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
			throw new UpdateApiException("UnauthorizedUpdateException");
		}
		return register(server, userid, data);
	}

	@SuppressWarnings({ "rawtypes" })
	public List eventQueue(String key, Object[] data) throws UpdateApiException {
		Server server = null;
		try {
			server = getAuthorizedServer(key, smartCast(data[0]), parseLong(data[1]).intValue());
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
			throw new UpdateApiException("UnauthorizedUpdateException");
		}
		return eventQueue(server);
	}
	
	/**
	 * 
	 * @param key
	 * @param list
	 * @throws UpdateApiException 
	 */
	public void confirmEvent(String key, Object[] list, Object[] data) throws UpdateApiException {
		Server server = null;
		try {
			server = getAuthorizedServer(key, smartCast(data[0]), parseLong(data[1]).intValue());
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
			throw new UpdateApiException("UnauthorizedUpdateException");
		}
		confirmEvent(server, list);
		
	}
}