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

		Server server = ServerManager.getAuthorizedServer("UIDTEST1","127.0.0.1");
		
		List<PlayerInfo> list = new ArrayList<PlayerInfo>();
		
		String banInfo = "pb::1306784244::-1::Test: reason";
		int[] count = new int[1];
		list.add(new PlayerInfo(Events.CONNECT, "player1", "guid1", 1L, "127.0.0.1", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player2", "guid2", 1L, "127.0.0.2", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player3", "guid3", 1L, "127.0.0.3", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player4", "guid4", 1L, "127.0.0.4", 1L));
		//list.add(new PlayerInfo(Events.ADDNOTE, "player5", "guid5", 1L, "127.0.0.5", 1L, "Una Nota"));
		//list.add(new PlayerInfo(Events.ADDNOTE, "player6", "guid6", 1L, "127.0.0.6", 1L, "Una Nota"));
		list.add(new PlayerInfo(Events.DELNOTE, "player5", "guid5", 1L, "127.0.0.7", 1L));
		list.add(new PlayerInfo(Events.UPDATE, "player2", "guid2", 1L, "127.0.0.5", 1L));
		//list.add(new PlayerInfo(Events.BAN, "player1", "guid1", 1L, "127.0.0.1", 1L, banInfo));
		//list.add(new PlayerInfo(Events.BAN, "player2-2", "guid2", 1L, "127.0.0.99", 1L, banInfo));
		list.add(new PlayerInfo(Events.UNBAN, "player2-3", "guid2", 1L, "127.0.0.98", 1L));
		list.add(new PlayerInfo(Events.DISCONNECT, "player4-1", "guid4", 1L, "127.0.0.55", 1L));
		
		Update update = new Update();
		update.updatePlayer(server, list);
		
		app.refreshServerInfo(server);
		
		assertEquals(1, server.getOnlinePlayers());
		assertEquals(1, app.bannedQuery(0, 1000, count).size());
		assertEquals(6, app.rootQuery(0, 1000, count).size());
		
	}
	
	@After
	protected void tearDown() throws Exception {
		helper.tearDown();
	}
}
