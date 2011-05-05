package jipdbs.api.v2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jipdbs.api.Events;
import jipdbs.api.ServerManager;
import jipdbs.bean.BanInfo;
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
import jipdbs.util.MailAdmin;
import jipdbs.util.NGrams;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Update {

	private static final Logger log = Logger.getLogger(Update.class.getName());

	protected final ServerDAO serverDAO = new ServerCachedDAO(
			new ServerDAOImpl());
	protected final PlayerDAO playerDAO = new PlayerCachedDAO(
			new PlayerDAOImpl());
	protected final AliasDAO aliasDAO = new AliasCachedDAO(new AliasDAOImpl());

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
	 * @param version
	 *            the server's B3 plugin version. Can be null.
	 * @since 0.5
	 */
	public void updateName(String key, String name, String version,
			String remoteAddr) {

		try {
			ServerManager serverManager = new ServerManager();
			Server server = serverManager.getAuthorizedServer(key, remoteAddr,
					name);
			server.setName(name);
			server.setUpdated(new Date());
			server.setPluginVersion(version);
			serverDAO.save(server);

		} catch (UnauthorizedUpdateException e) {
			MailAdmin.sendMail("WARN", e.getMessage());
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
	 * Update player info
	 * 
	 * @param server
	 *            the server instance
	 * @param list
	 *            a list of jipdbs.bean.PlayerInfo
	 * @since 0.5
	 */
	public void updatePlayer(Server server, List<PlayerInfo> list) {
		
		try {
			Map<String, Entity> entities = new HashMap<String, Entity>(15);
			int connected = server.getOnlinePlayers();
			for (PlayerInfo playerInfo : list) {
				try {
					Date playerLastUpdate;
					Player player = playerDAO.findByServerAndGuid(server.getKey(),
							playerInfo.getGuid());
					if (player == null) {
						player = new Player();
						player.setCreated(playerInfo.getUpdated());
						player.setGuid(playerInfo.getGuid());
						player.setLevel(playerInfo.getLevel());
						player.setClientId(playerInfo.getClientId());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						player.setBanInfoUpdated(null);
						playerLastUpdate = playerInfo.getUpdated();
					} else {
						player.setLevel(playerInfo.getLevel());
						if (player.getClientId() == null) {
							player.setClientId(playerInfo.getClientId());
						}
						playerLastUpdate = player.getUpdated();
					}

					if (Events.BAN.equals(playerInfo.getEvent())) {
						BanInfo banInfo = new BanInfo(playerInfo.getExtra());
						player.setBanInfo(banInfo.toString());
						player.setBanInfoUpdated(playerInfo.getUpdated());
						player.setConnected(false);
						connected += -1;
					} else if (Events.CONNECT.equals(playerInfo.getEvent())
							|| Events.DISCONNECT.equals(playerInfo.getEvent())
							|| Events.UNBAN.equals(playerInfo.getEvent())
							|| Events.UPDATE.equals(playerInfo.getEvent())) {
						player.setBanInfo(null);
						player.setBanInfoUpdated(null);
						if (Events.CONNECT.equals(playerInfo.getEvent()) || Events.UPDATE.equals(playerInfo.getEvent())) {
							player.setConnected(true);
							if (Events.CONNECT.equals(playerInfo.getEvent())) {
								connected += 1;	
							}
						} else if (Events.DISCONNECT.equals(playerInfo.getEvent())) {
							player.setConnected(false);
							connected += -1;
						}
					} else if (Events.ADDNOTE.equals(playerInfo.getEvent())) {
						player.setNote(playerInfo.getExtra());
					} else if (Events.DELNOTE.equals(playerInfo.getEvent())) {
						player.setNote(null);
					}
					player.setUpdated(playerInfo.getUpdated());

					playerDAO.save(player, player.getKey() == null);
					entities.put("player-" + player.getGuid(), player.toEntity());

					String aliasKey = "alias-" + player.getGuid()
							+ playerInfo.getName() + playerInfo.getIp();

					Entity aliasEntity = entities.get(aliasKey);
					Alias alias;
					if (aliasEntity != null) {
						alias = new Alias(aliasEntity);
					} else {
						alias = aliasDAO.findByPlayerAndNicknameAndIp(
								player.getKey(), playerInfo.getName(),
								playerInfo.getIp());
					}

					if (alias == null) {
						alias = new Alias();
						alias.setCount(1L);
						alias.setCreated(playerInfo.getUpdated());
						alias.setNickname(playerInfo.getName());
						alias.setNgrams(NGrams.ngrams(playerInfo.getName()));
						alias.setPlayer(player.getKey());
						alias.setIp(playerInfo.getIp());
						alias.setServer(server.getKey());
						alias.setUpdated(playerInfo.getUpdated());
					} else {
						if (Events.CONNECT.equals(playerInfo.getEvent())) {
							if (server.getUpdated() == null
									|| server.getUpdated().after(playerLastUpdate)) {
								alias.setCount(alias.getCount() + 1L);
							}
						}
						alias.setUpdated(playerInfo.getUpdated());
					}
					aliasDAO.save(alias, false);
					entities.put(aliasKey, alias.toEntity());
				} catch (Exception e) {
					log.severe(e.getMessage());
					StringWriter w = new StringWriter();
					e.printStackTrace(new PrintWriter(w));
					log.severe(w.getBuffer().toString());
				}
			}
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();
			service.put(entities.values());
			server.setUpdated(new Date());
			if (connected < 0) connected = 0; // just in case
			server.setOnlinePlayers(connected);
			serverDAO.save(server);
		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

}
