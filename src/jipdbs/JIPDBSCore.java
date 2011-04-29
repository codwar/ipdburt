package jipdbs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jipdbs.api.ServerManager;
import jipdbs.api.v2.Update;
import jipdbs.bean.PlayerInfo;
import jipdbs.data.Alias;
import jipdbs.data.AliasCachedDAO;
import jipdbs.data.AliasDAO;
import jipdbs.data.AliasDAOImpl;
import jipdbs.data.Player;
import jipdbs.data.PlayerCachedDAO;
import jipdbs.data.PlayerDAO;
import jipdbs.data.PlayerDAOImpl;
import jipdbs.data.Server;
import jipdbs.data.ServerCachedDAO;
import jipdbs.data.ServerDAO;
import jipdbs.data.ServerDAOImpl;
import jipdbs.exception.UnauthorizedUpdateException;
import jipdbs.util.NGrams;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class JIPDBSCore {

	private static final Logger log = Logger.getLogger(JIPDBSCore.class
			.getName());

	protected final ServerDAO serverDAO = new ServerCachedDAO(
			new ServerDAOImpl());
	protected final PlayerDAO playerDAO = new PlayerCachedDAO(
			new PlayerDAOImpl());
	protected final AliasDAO aliasDAO = new AliasCachedDAO(new AliasDAOImpl());

	public void start() {
		// Reserved.
	}

	public void stop() {
		// Reserved.
	}

	/**
	 * Updates the name of a server given its uid.
	 * <p>
	 * Invoked by the servers when they change their public server name.
	 * 
	 * @param key
	 *            the server uid.
	 * @param name
	 *            the server's new name.
	 * @param remoteAddr
	 *            the server's remote address.
	 * @deprecated use the four arguments version.
	 */
	@Deprecated
	public void updateName(String key, String name, String remoteAddr) {
		Update newApi = new Update();
		newApi.updateName(key, name, null, remoteAddr);
	}

	/**
	 * Updates the current list of players for a given server.
	 * 
	 * @param key
	 *            the server key.
	 * @param list
	 *            the list of currently logged in players.
	 * @since 0.1
	 */
	@Deprecated
	public void updateConnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		ServerManager serverManager = new ServerManager();
		
		try {
			Date stamp = new Date();

			Server server = serverManager.getAuthorizedServer(key, remoteAddr);

				log.info("Processing " + server.getName());

				Map<String, Entity> entities = new HashMap<String, Entity>();

				for (PlayerInfo info : list) {
					String playerKey = "player-" + key + info.getGuid();

					Player player = playerDAO.findByServerAndGuid(server.getKey(), info.getGuid());

					Date playerLastUpdate = null;
					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						player.setBanInfoUpdated(null);
						if (info.getUpdated() != null) {
							player.setUpdated(info.getUpdated());
						} else {
							player.setUpdated(stamp);
						}
						playerDAO.save(player);
					} else {
						if (player.getBanInfo() != null) {
							player.setBanInfo(null);
							player.setBanInfoUpdated(null);
						}
						playerLastUpdate = player.getUpdated();
						player.setUpdated(stamp);
						playerDAO.save(player, false);
						entities.put(playerKey, player.toEntity());
					}

					String aliasKey = "alias-" + player.getGuid() + info.getName() + info.getIp();

					Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(player.getKey(), info.getName(), info.getIp());

					if (alias == null) {
						alias = new Alias();
						alias.setCount(1);
						alias.setCreated(stamp);
						alias.setNickname(info.getName());
						alias.setNgrams(NGrams.ngrams(info.getName()));
						alias.setPlayer(player.getKey());
						alias.setIp(info.getIp());
						alias.setServer(server.getKey());
						if (info.getUpdated() != null) {
							alias.setUpdated(info.getUpdated());
						} else {
							alias.setUpdated(stamp);
						}
					} else {
						if (server.getUpdated() == null
								|| playerLastUpdate == null
								|| server.getUpdated().after(playerLastUpdate)) {
							alias.setCount(alias.getCount() + 1);
						}
						alias.setUpdated(stamp);
						aliasDAO.save(alias, false);
					}
					entities.put(aliasKey, alias.toEntity());
				}
				server.setUpdated(stamp);
				service.put(entities.values());
				serverDAO.save(server);
		} catch (UnauthorizedUpdateException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());				
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	/**
	 * @since 0.2
	 */
	@Deprecated
	public void updateDisconnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		ServerManager serverManager = new ServerManager();
		try {
			Date stamp = new Date();

			Server server = serverManager.getAuthorizedServer(key, remoteAddr);

				log.info("Processing " + server.getName());

				Map<String, Entity> entities = new HashMap<String, Entity>();

				for (PlayerInfo info : list) {
					String playerKey = "player-" + key + info.getGuid();
					Player player = playerDAO.findByServerAndGuid(server.getKey(), info.getGuid());

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setUpdated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						player.setBanInfoUpdated(null);
						playerDAO.save(player);
					} else {
						player.setUpdated(stamp);
						playerDAO.save(player, false);
						entities.put(playerKey, player.toEntity());
					}

					String aliasKey = "alias-" + player.getGuid() + info.getName() + info.getIp();
					Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(player.getKey(), info.getName(), info.getIp());

					if (alias == null) {
						alias = new Alias();
						alias.setCount(1);
						alias.setCreated(stamp);
						alias.setNickname(info.getName());
						alias.setNgrams(NGrams.ngrams(info.getName()));
						alias.setPlayer(player.getKey());
						alias.setIp(info.getIp());
						alias.setUpdated(stamp);
						alias.setServer(server.getKey());
						entities.put(aliasKey, alias.toEntity());
					} else {
						alias.setUpdated(stamp);
						aliasDAO.save(alias, false);
					}
				}
				server.setUpdated(stamp);
				service.put(entities.values());
				serverDAO.save(server);
		} catch (UnauthorizedUpdateException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());				
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
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
	 * @since 0.1
	 */
	@Deprecated
	public void updateBanInfo(String key, List<BanInfo> list, String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		ServerManager serverManager = new ServerManager();
		try {

			Date stamp = new Date();

			Server server = serverManager.getAuthorizedServer(key, remoteAddr);

				log.info("Processing " + server.getName());

				Map<String, Entity> entities = new HashMap<String, Entity>();

				for (BanInfo info : list) {

					Player player = playerDAO.findByServerAndGuid(server.getKey(), info.getGuid());

					String reason = info.getReason();
					Date banInfoUpdated = info.getUpdated() != null ? info
							.getUpdated() : new Date();

					if (reason.isEmpty())
						reason = null;

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						if (info.getUpdated() != null)
							player.setUpdated(info.getUpdated());
						player.setBanInfo(reason);
						player.setBanInfoUpdated(reason != null ? banInfoUpdated
								: null);
						playerDAO.save(player);
					} else {
						if (info.getUpdated() != null)
							player.setUpdated(info.getUpdated());
						player.setBanInfo(reason);
						player.setBanInfoUpdated(reason != null ? banInfoUpdated
								: null);
						entities.put("player-" + player.getGuid(),
								player.toEntity());
						playerDAO.save(player, false);
					}

					String aliasKey = "alias-" + player.getGuid()+ info.getName() + info.getIp();
					Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(player.getKey(), info.getName(), info.getIp());
					
					if (alias == null) {
						alias = new Alias();
						alias.setCount(1);
						alias.setCreated(stamp);
						alias.setNickname(info.getName());
						alias.setNgrams(NGrams.ngrams(info.getName()));
						alias.setPlayer(player.getKey());
						alias.setIp(info.getIp());
						if (info.getUpdated() != null) {
							alias.setUpdated(info.getUpdated());
						}
						alias.setServer(server.getKey());
						entities.put(aliasKey, alias.toEntity());
					} else {
						if (info.getUpdated() != null) {
							alias.setUpdated(info.getUpdated());
							entities.put(aliasKey, alias.toEntity());
						}
						aliasDAO.save(alias, false);
					}
				}
				service.put(entities.values());
				
		} catch (UnauthorizedUpdateException e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());				
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

}
