package jipdbs;

import java.util.Date;
import java.util.List;

import jipdbs.data.Server;
import jipdbs.data.ServerDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Transaction;

public class JIPDBS {

	private ServerDAO serverDAO = new ServerDAO();

	public void start() {

	}

	public void stop() {

	}

	public void updateServer(String name, String admin, String uid) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Server server = new Server();
		server.setAdmin(new Email(admin));
		server.setCreated(new Date());
		server.setUpdated(new Date());
		server.setUid(uid);
		server.setName(name);

		Transaction tx = service.beginTransaction();
		try {
			serverDAO.insert(service, server);
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();
		}
	}

	public List<Server> getServers() {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return serverDAO.getAll(service);
	}

	public void updateName(String key, String name) {
		// TODO Auto-generated method stub
		
	}

	public void insertLog(String key, List<PlayerInfo> list) {
		// TODO Auto-generated method stub
		
	}

	public void updateBanInfo(String key, List<BanInfo> list) {
		// TODO Auto-generated method stub
		
	}
}
