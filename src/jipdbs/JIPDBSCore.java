package jipdbs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
import jipdbs.util.MailAdmin;
import jipdbs.util.NGrams;

import org.datanucleus.util.StringUtils;

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
		this.updateName(key, name, null, remoteAddr);
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
	 * @param version
	 *            the server's B3 plugin version. Can be null.
	 * @since 0.1
	 */
	public void updateName(String key, String name, String version,
			String remoteAddr) {

		try {

			Server server = getAuthorizedServer(key, remoteAddr, name);
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

	public Server getAuthorizedServer(String key, String remoteAddress)
			throws UnauthorizedUpdateException {
		return getAuthorizedServer(key, remoteAddress, null);
	}

	private Server getAuthorizedServer(String key, String remoteAddress,
			String serverName) throws UnauthorizedUpdateException {

		Server server = serverDAO.findByUid(key);

		if (server == null) {

			// Compose.
			StringBuilder builder = new StringBuilder(
					"Se intenta actualizar servidor no existente.\n");
			builder.append("Key: " + key).append("\n");
			if (serverName != null)
				builder.append("Nombre: " + serverName).append("\n");
			if (remoteAddress != null)
				builder.append("IP: " + remoteAddress).append("\n");
			String message = builder.toString();

			// Throw.
			throw new UnauthorizedUpdateException(message);
		}

		if (remoteAddress != null && StringUtils.notEmpty(server.getAddress())
				&& !remoteAddress.equals(server.getAddress())) {

			// Compose.
			StringBuilder builder = new StringBuilder(
					"Intento de actualizar desde IP no autorizada.\n");
			builder.append("Key: " + key).append("\n");
			if (serverName != null)
				builder.append("Nombre: " + serverName).append("\n");
			if (remoteAddress != null)
				builder.append("IP: " + remoteAddress).append("\n");
			String message = builder.toString();

			// Throw.
			throw new UnauthorizedUpdateException(message);
		}

		return server;
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
	public void updateConnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		try {
			Server server = getAuthorizedServer(key, remoteAddr);

			log.info("Processing " + server.getName());

			for (PlayerInfo info : list) {
				Player player = playerDAO.findByServerAndGuid(server.getKey(),
						info.getGuid());

				Date updated = new Date();
				if (info.getUpdated() != null)
					updated = info.getUpdated();

				Date playerLastUpdate = updated;

				if (player == null) {
					player = new Player();
					player.setCreated(updated);
					player.setGuid(info.getGuid());
					player.setServer(server.getKey());
					player.setBanInfo(null);
					player.setBanInfoUpdated(null);
				} else {
					if (player.getBanInfo() != null) {
						player.setBanInfo(null);
						player.setBanInfoUpdated(null);
					}
					playerLastUpdate = player.getUpdated();
				}

				player.setUpdated(updated);
				playerDAO.save(player);

				Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
						player.getKey(), info.getName(), info.getIp());

				if (alias == null) {

					alias = new Alias();

					alias.setCount(1);
					alias.setCreated(updated);
					alias.setNickname(info.getName());
					alias.setNgrams(NGrams.ngrams(info.getName()));
					alias.setPlayer(player.getKey());
					alias.setIp(info.getIp());
					alias.setServer(server.getKey());

				} else {
					if (server.getUpdated() == null
							|| server.getUpdated().after(playerLastUpdate)) {
						alias.setCount(alias.getCount() + 1);
					}
				}

				alias.setUpdated(updated);
				aliasDAO.save(alias);
			}

			server.setUpdated(new Date());
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
	 * @since 0.2
	 */
	public void updateDisconnect(String key, List<PlayerInfo> list,
			String remoteAddr) {

		try {
			Server server = getAuthorizedServer(key, remoteAddr);

			log.info("Processing " + server.getName());

			for (PlayerInfo info : list) {

				Date updated = new Date();
				if (info.getUpdated() != null)
					updated = info.getUpdated();

				Date playerLastUpdate = updated;

				Player player = playerDAO.findByServerAndGuid(server.getKey(),
						info.getGuid());

				if (player == null) {

					player = new Player();
					player.setCreated(updated);
					player.setGuid(info.getGuid());
					player.setServer(server.getKey());
					player.setBanInfo(null);
					player.setBanInfoUpdated(null);

				} else {
					playerLastUpdate = player.getUpdated();
				}

				player.setUpdated(updated);
				playerDAO.save(player);

				Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
						player.getKey(), info.getName(), info.getIp());

				if (alias == null) {
					alias = new Alias();
					alias.setCount(1);
					alias.setCreated(updated);
					alias.setNickname(info.getName());
					alias.setNgrams(NGrams.ngrams(info.getName()));
					alias.setPlayer(player.getKey());
					alias.setIp(info.getIp());
					alias.setServer(server.getKey());
				} else {
					if (server.getUpdated() == null
							|| server.getUpdated().after(playerLastUpdate)) {
						alias.setCount(alias.getCount() + 1);
					}
				}

				alias.setUpdated(updated);
				aliasDAO.save(alias);

			}

			server.setUpdated(new Date());
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
	public void updateBanInfo(String key, List<BanInfo> list, String remoteAddr) {

		try {

			Server server = getAuthorizedServer(key, remoteAddr);

			log.info("Processing " + server.getName());

			for (BanInfo info : list) {

				Date updated = new Date();
				if (info.getUpdated() != null)
					updated = info.getUpdated();

				Date playerLastUpdate = updated;

				Player player = playerDAO.findByServerAndGuid(server.getKey(),
						info.getGuid());

				String reason = info.getReason();

				if (reason.isEmpty())
					reason = null;

				if (player == null) {
					player = new Player();
					player.setCreated(updated);
					player.setGuid(info.getGuid());
					player.setServer(server.getKey());
				}

				player.setBanInfo(reason);
				player.setBanInfoUpdated(reason != null ? updated : null);

				player.setUpdated(updated);
				playerDAO.save(player);

				// Save the last alias.

				Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
						player.getKey(), info.getName(), info.getIp());

				if (alias == null) {
					alias = new Alias();
					alias.setCount(1);
					alias.setCreated(updated);
					alias.setNickname(info.getName());
					alias.setNgrams(NGrams.ngrams(info.getName()));
					alias.setPlayer(player.getKey());
					alias.setIp(info.getIp());
					alias.setServer(server.getKey());
				} else {
					if (server.getUpdated() == null
							|| server.getUpdated().after(playerLastUpdate)) {
						alias.setCount(alias.getCount() + 1);
					}
				}

				alias.setUpdated(updated);
				aliasDAO.save(alias);
			}

			server.setUpdated(new Date());
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
	 * @since 0.5
	 */
	public void playerConnected(Server server, String nickname, String ip,
			String guid, Date updated, Integer id, Integer level) {

		try {

			if (updated == null)
				updated = new Date();
			Date playerLastUpdate = updated;

			Player player = playerDAO
					.findByServerAndGuid(server.getKey(), guid);

			// New player.
			if (player == null) {

				player = new Player();

				player.setCreated(updated);
				player.setGuid(guid);
				player.setServer(server.getKey());
				player.setBanInfo(null);
				player.setBanInfoUpdated(null);
				player.setId(id);

			}
			// Known player.
			else {

				// Player was allowed to connect by the server, that means that
				// the ban has gone.
				if (player.getBanInfo() != null) {
					player.setBanInfo(null);
					player.setBanInfoUpdated(null);
				}

				playerLastUpdate = player.getUpdated();
			}

			// Level might be changed.
			player.setLevel(level);
			player.setUpdated(updated);
			playerDAO.save(player);

			Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
					player.getKey(), nickname, ip);

			if (alias == null) {
				alias = new Alias();
				alias.setCount(1);
				alias.setCreated(updated);
				alias.setNickname(nickname);
				alias.setNgrams(NGrams.ngrams(nickname));
				alias.setPlayer(player.getKey());
				alias.setIp(ip);
				alias.setServer(server.getKey());
			} else {
				if (server.getUpdated() == null
						|| server.getUpdated().after(playerLastUpdate)) {
					alias.setCount(alias.getCount() + 1);
				}
			}

			alias.setUpdated(updated);
			aliasDAO.save(alias);

			server.setUpdated(updated);
			serverDAO.save(server);

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	/**
	 * @since 0.5
	 */
	public void playerUpdated(Server server, String nickname, String ip,
			String guid, Date updated, Integer id, Integer level) {

		try {

			if (updated == null)
				updated = new Date();
			Date playerLastUpdate = updated;

			Player player = playerDAO
					.findByServerAndGuid(server.getKey(), guid);

			// New player. Possibly the connected event was missed.
			if (player == null) {

				player = new Player();

				player.setCreated(updated);
				player.setGuid(guid);
				player.setServer(server.getKey());
				player.setBanInfo(null);
				player.setBanInfoUpdated(null);
				player.setId(id);

			}
			// Known player.
			else {

				// Player was allowed to connect by the server, that means that
				// the ban has gone.
				if (player.getBanInfo() != null) {
					player.setBanInfo(null);
					player.setBanInfoUpdated(null);
				}
				playerLastUpdate = player.getUpdated();
			}

			// Level might be changed.
			player.setLevel(level);
			player.setUpdated(updated);
			playerDAO.save(player);

			// Save the new alias.

			Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
					player.getKey(), nickname, ip);

			if (alias == null) {
				alias = new Alias();
				alias.setCount(1);
				alias.setCreated(updated);
				alias.setNickname(nickname);
				alias.setNgrams(NGrams.ngrams(nickname));
				alias.setPlayer(player.getKey());
				alias.setIp(ip);
				alias.setServer(server.getKey());
			} else {
				if (server.getUpdated() == null
						|| server.getUpdated().after(playerLastUpdate)) {
					alias.setCount(alias.getCount() + 1);
				}
			}

			alias.setUpdated(updated);
			aliasDAO.save(alias);

			server.setUpdated(updated);
			serverDAO.save(server);

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	/**
	 * @since 0.5
	 */
	public void playerDisconnected(Server server, String nickname, String ip,
			String guid, Date updated, Integer id, Integer level) {

		try {

			if (updated == null)
				updated = new Date();
			Date playerLastUpdate = updated;

			Player player = playerDAO
					.findByServerAndGuid(server.getKey(), guid);

			// New player. Possibly the connected event was missed.
			if (player == null) {

				player = new Player();

				player.setCreated(updated);
				player.setGuid(guid);
				player.setServer(server.getKey());
				player.setBanInfo(null);
				player.setBanInfoUpdated(null);
				player.setId(id);

			} else {
				playerLastUpdate = player.getUpdated();
			}

			// Level might be changed.
			player.setLevel(level);
			player.setUpdated(updated);
			playerDAO.save(player);

			// Alias might be missing. Save.

			Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
					player.getKey(), nickname, ip);

			if (alias == null) {
				alias = new Alias();
				alias.setCount(1);
				alias.setCreated(updated);
				alias.setNickname(nickname);
				alias.setNgrams(NGrams.ngrams(nickname));
				alias.setPlayer(player.getKey());
				alias.setIp(ip);
				alias.setServer(server.getKey());
			} else {
				if (server.getUpdated() == null
						|| server.getUpdated().after(playerLastUpdate)) {
					alias.setCount(alias.getCount() + 1);
				}
			}

			alias.setUpdated(updated);
			aliasDAO.save(alias);

			server.setUpdated(updated);
			serverDAO.save(server);

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	/**
	 * @since 0.5
	 */
	public void playerBanned(Server server, String nickname, String ip,
			String guid, Date updated, Integer id, Integer level, String baninfo) {

		try {

			if (updated == null)
				updated = new Date();
			Date playerLastUpdate = updated;

			Player player = playerDAO
					.findByServerAndGuid(server.getKey(), guid);

			// New player. Possibly the connected event was missed.
			if (player == null) {

				player = new Player();

				player.setCreated(updated);
				player.setGuid(guid);
				player.setServer(server.getKey());
				player.setBanInfo(null);
				player.setBanInfoUpdated(null);
				player.setId(id);

			} else {
				playerLastUpdate = player.getUpdated();
			}

			player.setBanInfo(baninfo);
			player.setBanInfoUpdated(updated);
			// Level might be changed.
			player.setLevel(level);
			player.setUpdated(updated);
			playerDAO.save(player);

			// Alias might be missing. Save.

			Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
					player.getKey(), nickname, ip);

			if (alias == null) {
				alias = new Alias();
				alias.setCount(1);
				alias.setCreated(updated);
				alias.setNickname(nickname);
				alias.setNgrams(NGrams.ngrams(nickname));
				alias.setPlayer(player.getKey());
				alias.setIp(ip);
				alias.setServer(server.getKey());
			} else {
				if (server.getUpdated() == null
						|| server.getUpdated().after(playerLastUpdate)) {
					alias.setCount(alias.getCount() + 1);
				}
			}

			alias.setUpdated(updated);
			aliasDAO.save(alias);

			server.setUpdated(updated);
			serverDAO.save(server);

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}

	/**
	 * @since 0.5
	 */
	public void playerUnbanned(Server server, String nickname, String ip,
			String guid, Date updated, Integer id, Integer level) {

		try {

			if (updated == null)
				updated = new Date();
			Date playerLastUpdate = updated;

			Player player = playerDAO
					.findByServerAndGuid(server.getKey(), guid);

			// New player. Possibly the connected event was missed.
			if (player == null) {

				player = new Player();

				player.setCreated(updated);
				player.setGuid(guid);
				player.setServer(server.getKey());
				player.setBanInfo(null);
				player.setBanInfoUpdated(null);
				player.setId(id);

			} else {
				playerLastUpdate = updated;
			}

			player.setBanInfo(null);
			player.setBanInfoUpdated(null);

			// Level might be changed.
			player.setLevel(level);
			player.setUpdated(updated);
			playerDAO.save(player);

			// Alias might be missing. Save.

			Alias alias = aliasDAO.findByPlayerAndNicknameAndIp(
					player.getKey(), nickname, ip);

			if (alias == null) {
				alias = new Alias();
				alias.setCount(1);
				alias.setCreated(updated);
				alias.setNickname(nickname);
				alias.setNgrams(NGrams.ngrams(nickname));
				alias.setPlayer(player.getKey());
				alias.setIp(ip);
				alias.setServer(server.getKey());
			} else {
				if (server.getUpdated() == null
						|| server.getUpdated().after(playerLastUpdate)) {
					alias.setCount(alias.getCount() + 1);
				}
			}

			alias.setUpdated(updated);
			aliasDAO.save(alias);

			server.setUpdated(updated);
			serverDAO.save(server);

		} catch (Exception e) {
			log.severe(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.severe(w.getBuffer().toString());
		}
	}
}
