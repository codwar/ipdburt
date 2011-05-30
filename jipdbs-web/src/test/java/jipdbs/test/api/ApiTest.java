package jipdbs.test.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jipdbs.api.Events;
import jipdbs.api.ServerManager;
import jipdbs.api.v2.Update;
import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;
import jipdbs.core.model.dao.ServerDAO;
import jipdbs.core.model.dao.impl.ServerDAOImpl;
import jipdbs.info.PlayerInfo;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ApiTest extends TestCase {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private JIPDBS app;
	
	@Before
	protected void setUp() throws Exception {
		helper.setUp();
		app = new JIPDBS(new Properties());
		app.addServer("TEST1", "admin@admin.com", "UIDTEST1", null);
	}

	public void testUpdateServer() {
		Update update = new Update();
		update.updateName("UIDTEST1", "SERVER-TEST", "version", "127.0.0.1");
		
		ServerDAO serverDAO = new ServerDAOImpl();
		Server server = serverDAO.findByUid("UIDTEST1");

		assertEquals("SERVER-TEST", server.getName());
	}
	
	public void testUpdateClient() throws Exception {

		ServerManager serverManager = new ServerManager();
		Server server = serverManager.getAuthorizedServer("UIDTEST1","127.0.0.1");
		
		List<PlayerInfo> list = new ArrayList<PlayerInfo>();
		list.add(new PlayerInfo(Events.CONNECT, "player1", "guid1", 1L, "127.0.0.1", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player2", "guid2", 1L, "127.0.0.2", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player3", "guid3", 1L, "127.0.0.3", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player4", "guid4", 1L, "127.0.0.4", 1L));

		PlayerInfo p = new PlayerInfo(Events.BAN, "player1", "guid1", 1L, "127.0.0.1", 1L);
		p.setExtra("pb::1306784244::-1::Test: reason");
		list.add(p);
		
		Update update = new Update();
		update.updatePlayer(server, list);
		
		app.refreshServerInfo(server);
		
		assertEquals(3, server.getOnlinePlayers());
		
	}
	
	@After
	protected void tearDown() throws Exception {
		helper.tearDown();
	}
}
