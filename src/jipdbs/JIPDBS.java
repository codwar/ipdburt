package jipdbs;

import java.util.Date;
import java.util.List;

import jipdbs.data.Alias;
import jipdbs.data.AliasDAO;
import jipdbs.data.Player;
import jipdbs.data.PlayerDAO;
import jipdbs.data.Server;
import jipdbs.data.ServerDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Transaction;

public class JIPDBS {

	private final ServerDAO serverDAO = new ServerDAO();
	private final PlayerDAO playerDAO = new PlayerDAO();
	private final AliasDAO aliasDAO = new AliasDAO();

	public void start() {
		// Reserved.
	}

	public void stop() {
		// Reserved.
	}

	// xmlrpc methods //////////////////////////////////////////////////////////

	/**
	 * Updates the name of a server given its uid.
	 * <p>
	 * Invoked by the servers when they change their public server name.
	 * 
	 * @param key
	 *            the server uid.
	 * @param name
	 *            the server's new name.
	 */
	public void updateName(String key, String name) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Transaction tx = service.beginTransaction();
		try {
			Server server = serverDAO.findByUid(service, key);

			if (server != null) {
				server.setName(name);
				serverDAO.save(service, server);
				tx.commit();
			}

			// If the server doesn't exist do nothing.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive())
				tx.rollback();
		}
	}

	/**
	 * Updates the current list of players for a given server.
	 * 
	 * @param key
	 *            the server key.
	 * @param list
	 *            the list of currently logged in players.
	 */
	public void insertLog(String key, List<PlayerInfo> list) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

//		Transaction tx = service.beginTransaction();

		try {

			Date stamp = new Date();

			Server server = serverDAO.findByUid(service, key);

			if (server != null) {

				server.setUpdated(stamp);
				serverDAO.save(service, server);

				for (PlayerInfo info : list) {

					Player player = playerDAO.findByServerAndGuid(service,
							server.getKey(), info.getGuid());

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						player.setBanInfo(null);
					}

					player.setUpdated(stamp);
					playerDAO.save(service, player);

					Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(service,
							player.getKey(), info.getName(), info.getIp());

					if (alias == null) {
						alias = new Alias();
						alias.setCount(0);
						alias.setCreated(stamp);
						alias.setNickname(info.getName());
						alias.setPlayer(player.getKey());
						alias.setIp(info.getIp());
					}

					alias.setCount(alias.getCount() + 1);
					alias.setUpdated(stamp);

					aliasDAO.save(service, alias);
				}

//				tx.commit();
			}

			// If the server doesn't exist do nothing.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (tx.isActive())
//				tx.rollback();
		}
	}

	/**
	 * Bulk updates banning information for the given players.
	 * 
	 * @param key
	 *            the server uid.
	 * @param list
	 *            the ban information list. If {@link BanInfo#getReason()} is
	 *            null or empty, stores <code>null</code> y the datastore.
	 *            That's in order to unban a player.
	 */
	public void updateBanInfo(String key, List<BanInfo> list) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

//		Transaction tx = service.beginTransaction();

		try {

			Date stamp = new Date();

			Server server = serverDAO.findByUid(service, key);

			if (server != null) {

				server.setUpdated(stamp);
				serverDAO.save(service, server);

				for (BanInfo info : list) {

					Player player = playerDAO.findByServerAndGuid(service,
							server.getKey(), info.getGuid());

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
					}

					String reason = info.getReason();

					if (reason.isEmpty())
						reason = null;

					player.setBanInfo(reason);
					player.setUpdated(stamp);
					playerDAO.save(service, player);
				}

//				tx.commit();
			}

			// If the server doesn't exist do nothing.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (tx.isActive())
//				tx.rollback();
		}
	}

	// web fron-end methods ////////////////////////////////////////////////////

	public void addServer(String name, String admin, String uid) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Server server = new Server();
		server.setAdmin(new Email(admin));
		server.setCreated(new Date());
		server.setUpdated(new Date());
		server.setUid(uid);
		server.setName(name);

		serverDAO.save(service, server);
	}

	public List<Server> getServers() {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return serverDAO.findAll(service);
	}
}
