package jipdbs.test.xmlrpc;

import junit.framework.TestCase;

import org.apache.xmlrpc.client.XmlRpcClient;

public class XmlRpcApiTest extends TestCase {

	private XmlRpcClient client;
	
//	protected void setUp() throws Exception {
//		/* CREATE TEST SERVER */
//		Server server = new Server();
//		server.setUid("TEST1");
//		
//		ServerDAO dao = new ServerDAOImpl();
//		dao.save(server);
//		
//		/* INIT RPC CONNECTION */
//		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//		config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc2"));
//		this.client = new XmlRpcClient();
//		this.client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
//		this.client.setConfig(config);
//
//	}
	
	public void testDummy() {
		assertEquals(true, true);
	}
	
//	public void testUpdateName() throws XmlRpcException {
//		/* update the server name */
//		String newName = "Test Name " + new Date().toString();
//		Object[] params = new Object[]{"TEST1", newName, "Test Version"};
//	    client.execute("updateName", params);
//	    
//	    /* check if the name was updated */
//	    ServerDAO dao = new ServerDAOImpl();
//	    Server server = dao.findByUid("TEST1");
//	    
//	    assertEquals(newName, server.getName());
//	    
//	}
	
}
