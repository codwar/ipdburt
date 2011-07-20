package jipdbs.test.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jipdbs.api.Events;
import jipdbs.api.ServerManager;
import jipdbs.api.v2.Update;
import jipdbs.core.JIPDBS;
import jipdbs.core.model.Penalty;
import jipdbs.core.model.Server;
import jipdbs.core.model.dao.ServerDAO;
import jipdbs.core.model.dao.impl.ServerDAOImpl;
import jipdbs.info.PenaltyInfo;
import jipdbs.info.PlayerInfo;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class ApiTest extends TestCase {

	private final LocalServiceTestConfig[] configs = {  
	        new LocalDatastoreServiceTestConfig(),  
	        new LocalTaskQueueTestConfig().setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml")};  
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(configs);
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
		
		PenaltyInfo banInfo = new PenaltyInfo();
		banInfo.setType(Penalty.BAN);
		banInfo.setCreated(new Date());
		banInfo.setDuration(120L);
		banInfo.setReason("Test: reason");
		banInfo.setAdmin("guid1");

		PenaltyInfo noteInfo = new PenaltyInfo();
		noteInfo.setType(Penalty.NOTICE);
		noteInfo.setCreated(new Date());
		noteInfo.setReason("Test: Notice");
		noteInfo.setAdmin("guid2");
		
		int[] count = new int[1];
		list.add(new PlayerInfo(Events.CONNECT, "player1", "guid1", 1L, "127.0.0.1", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player2", "guid2", 1L, "127.0.0.2", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player3", "guid3", 1L, "127.0.0.3", 1L));
		list.add(new PlayerInfo(Events.CONNECT, "player4", "guid4", 1L, "127.0.0.4", 1L));
		list.add(new PlayerInfo(Events.ADDNOTE, "player5", "guid5", 1L, "127.0.0.5", 1L).setPenaltyInfo(noteInfo));
		list.add(new PlayerInfo(Events.ADDNOTE, "player6", "guid6", 1L, "127.0.0.6", 1L).setPenaltyInfo(noteInfo));
		list.add(new PlayerInfo(Events.DELNOTE, "player5", "guid5", 1L, "127.0.0.7", 1L));
		list.add(new PlayerInfo(Events.UPDATE, "player2", "guid2", 1L, "127.0.0.5", 1L));
		list.add(new PlayerInfo(Events.BAN, "player1", "guid1", 1L, "127.0.0.1", 1L).setPenaltyInfo(banInfo));
		list.add(new PlayerInfo(Events.BAN, "player2-2", "guid2", 1L, "127.0.0.99", 1L).setPenaltyInfo(banInfo));
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
