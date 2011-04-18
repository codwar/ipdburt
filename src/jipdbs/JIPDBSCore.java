package jipdbs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jipdbs.data.Alias;
import jipdbs.data.AliasDAO;
import jipdbs.data.Player;
import jipdbs.data.PlayerDAO;
import jipdbs.data.Server;
import jipdbs.data.ServerDAO;
import jipdbs.util.LocalCache;
import jipdbs.util.MailAdmin;
import jipdbs.util.NGrams;

import org.datanucleus.util.StringUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class JIPDBSCore {

	private static final Logger log = Logger.getLogger(JIPDBSCore.class
			.getName());

	protected final ServerDAO serverDAO = new ServerDAO();
	protected final PlayerDAO playerDAO = new PlayerDAO();
	protected final AliasDAO aliasDAO = new AliasDAO();

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
	public void updateName(String key, String name, String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Transaction tx = service.beginTransaction();
		try {
			Server server = serverDAO.findByUid(service, key);

			if (server != null) {
				if (remoteAddr != null
						&& StringUtils.notEmpty(server.getAddress())) {
					if (!remoteAddr.equals(server.getAddress())) {
						log.warning("Unauthorized update " + key + ", " + name + ", " + remoteAddr);
						unauthorizedUpdate(key, name, remoteAddr);
						return;
					}
				}
				server.setName(name);
				server.setUpdated(new Date());
				serverDAO.save(service, server);
				tx.commit();
			} else {
				log.severe("Trying to update non existing server (" + key + ","
						+ name + ", " + remoteAddr + ")");
				nonExistingServer(key, name, remoteAddr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive())
				tx.rollback();
		}
	}

	private void unauthorizedUpdate(String key, String name, String remoteAddr) {
		StringBuilder message = new StringBuilder("Intento de actualizar desde IP no autorizada.\n");
		message.append("Nombre: " + name);
		message.append("\n");
		message.append("Key: " + key);
		message.append("\n");
		if (remoteAddr != null) {
			message.append("IP: " + remoteAddr);
			message.append("\n");
		}
		MailAdmin.sendMail("WARN", message.toString());
	}

	private void nonExistingServer(String key, String name, String remoteAddr) {
		StringBuilder message = new StringBuilder("Se intenta actualizar servidor no existente.\n");
		message.append("Nombre: " + name);
		message.append("\n");
		message.append("Key: " + key);
		message.append("\n");
		if (remoteAddr != null) {
			message.append("IP: " + remoteAddr);
			message.append("\n");
		}
		MailAdmin.sendMail("ERROR", message.toString());
	}

	/**
	 * Updates the current list of players for a given server.
	 * 
	 * @param key
	 *            the server key.
	 * @param list
	 *            the list of currently logged in players.
	 */
	public void updateConnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		try {
			Date stamp = new Date();

			Server server = serverDAO.findByUid(service, key);

			if (server != null) {

				if (remoteAddr != null
						&& StringUtils.notEmpty(server.getAddress())) {
					if (!remoteAddr.equals(server.getAddress())) {
						log.warning("Unauthorized update " + key + ", " + server.getName() + ", " + remoteAddr);
						//unauthorizedUpdate(key, server.getName(), remoteAddr);
						return;
					}
				}

				log.info("Processing " + server.getName());
				
				Map<String, Entity> entities = new HashMap<String, Entity>();

				for (PlayerInfo info : list) {
					String playerKey = "player-"
							+ KeyFactory.keyToString(server.getKey())
							+ info.getGuid();
					Player player = (Player) LocalCache.getInstance().get(
							playerKey);
					if (player == null) {
						player = playerDAO.findByServerAndGuid(service,
								server.getKey(), info.getGuid());
					}

					Date playerLastUpdate = null;
					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						if (info.getUpdated() != null) {
							player.setUpdated(info.getUpdated());
						} else {
							player.setUpdated(stamp);	
						}
						playerDAO.save(service, player);
						LocalCache.getInstance().put(playerKey, player);
					} else {
						if (player.getBanInfo() != null) {
							player.setBanInfo(null);
						}
						playerLastUpdate = player.getUpdated();
						player.setUpdated(stamp);
						entities.put(playerKey, player.toEntity());
						LocalCache.getInstance().put(playerKey, player);
					}

					String aliasKey = "alias-"
							+ KeyFactory.keyToString(player.getKey())
							+ info.getName() + info.getIp();
					Alias alias = (Alias) LocalCache.getInstance()
							.get(aliasKey);
					if (alias == null) {
						alias = aliasDAO.findByPlayerAndNicknameAndIp(service,
								player.getKey(), info.getName(), info.getIp());
					}

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
						LocalCache.getInstance().put(aliasKey, alias);
					}
					entities.put(aliasKey, alias.toEntity());
				}
				server.setUpdated(stamp);
				serverDAO.cache(server);
				entities.put("server", server.toEntity());
				service.put(entities.values());
			} else {
				log.severe("Trying to update non existing server (" + key + ")");
				//nonExistingServer(key, "-", remoteAddr);
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	public void updateDisconnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		try {
			Date stamp = new Date();

			Server server = serverDAO.findByUid(service, key);

			if (server != null) {

				if (remoteAddr != null
						&& StringUtils.notEmpty(server.getAddress())) {
					if (!remoteAddr.equals(server.getAddress())) {
						log.warning("Unauthorized update " + key + ", " + server.getName() + ", " + remoteAddr);
						return;
					}
				}

				log.info("Processing " + server.getName());
				
				Map<String, Entity> entities = new HashMap<String, Entity>();

				for (PlayerInfo info : list) {
					String playerKey = "player-"
							+ KeyFactory.keyToString(server.getKey())
							+ info.getGuid();
					Player player = (Player) LocalCache.getInstance().get(
							playerKey);
					if (player == null) {
						player = playerDAO.findByServerAndGuid(service,
								server.getKey(), info.getGuid());
					}

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setUpdated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						playerDAO.save(service, player);
						LocalCache.getInstance().put(playerKey, player);
					} else {
						player.setUpdated(stamp);
						entities.put(playerKey, player.toEntity());
						LocalCache.getInstance().put(playerKey, player);
					}

					String aliasKey = "alias-"
							+ KeyFactory.keyToString(player.getKey())
							+ info.getName() + info.getIp();
					Alias alias = (Alias) LocalCache.getInstance()
							.get(aliasKey);
					if (alias == null) {
						alias = aliasDAO.findByPlayerAndNicknameAndIp(service,
								player.getKey(), info.getName(), info.getIp());
					}

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
						LocalCache.getInstance().put(aliasKey, alias);	
					}
				}
				server.setUpdated(stamp);
				serverDAO.cache(server);
				entities.put("server", server.toEntity());
				service.put(entities.values());
			} else {
				log.severe("Trying to update non existing server (" + key + ")");
			}
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
	 */
	public void updateBanInfo(String key, List<BanInfo> list, String remoteAddr) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		try {

			Date stamp = new Date();

			Server server = serverDAO.findByUid(service, key);

			if (server != null) {

				if (remoteAddr != null
						&& StringUtils.notEmpty(server.getAddress())) {
					if (!remoteAddr.equals(server.getAddress())) {
						log.warning("Unauthorized update");
						return;
					}
				}

				log.info("Processing " + server.getName());
				
				Map<String, Entity> entities = new HashMap<String, Entity>();
				
				for (BanInfo info : list) {

					Player player = playerDAO.findByServerAndGuid(service,
							server.getKey(), info.getGuid());

					String reason = info.getReason();
					if (reason.isEmpty())
						reason = null;

					if (player == null) {
						player = new Player();
						player.setCreated(stamp);
						player.setGuid(info.getGuid());
						player.setServer(server.getKey());
						if (info.getUpdated()!=null) {
							player.setUpdated(info.getUpdated());
						}
						player.setBanInfo(reason);
						playerDAO.save(service, player);
					} else {
						if (info.getUpdated()!=null) {
							player.setUpdated(info.getUpdated());
						}						
						player.setBanInfo(reason);
						entities.put("player-" + player.getGuid(), player.toEntity());
					}
					
					String aliasKey = "alias-"
						+ KeyFactory.keyToString(player.getKey())
						+ info.getName() + info.getIp();
					Alias alias = (Alias) LocalCache.getInstance()
						.get(aliasKey);
					if (alias == null) {
						alias = aliasDAO.findByPlayerAndNicknameAndIp(service,
								player.getKey(), info.getName(), info.getIp());
					}
					if (alias == null) {
						alias = new Alias();
						alias.setCount(1);
						alias.setCreated(stamp);
						alias.setNickname(info.getName());
						alias.setNgrams(NGrams.ngrams(info.getName()));
						alias.setPlayer(player.getKey());
						alias.setIp(info.getIp());
						if (info.getUpdated()!=null) {
							alias.setUpdated(info.getUpdated());
						}
						alias.setServer(server.getKey());
						entities.put(aliasKey, alias.toEntity());
					} else {
						if (info.getUpdated()!=null) {
							alias.setUpdated(info.getUpdated());
							entities.put(aliasKey, alias.toEntity());
						}						
					}
				}
				service.put(entities.values());
			} else {
				log.severe("Trying to update non existing server (" + key + ")");
			}

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

}
