/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.api.v2;

import iddb.api.Events;
import iddb.api.ServerManager;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.core.model.util.AliasManager;
import iddb.core.util.MailManager;
import iddb.exception.UnauthorizedUpdateException;
import iddb.info.PlayerInfo;
import iddb.task.TaskManager;
import iddb.task.tasks.PenaltyTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Update {

	private static final Logger log = LoggerFactory.getLogger(Update.class); 

	protected final ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
	protected final AliasDAO aliasDAO = (AliasDAO) DAOFactory.forClass(AliasDAO.class);
	
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
	public void updateName(String key, String name, String version,	Integer permission, String remoteAddr) {
		try {
			Server server = ServerManager.getAuthorizedServer(key, remoteAddr,name);
			server.setName(name);
			server.setPermission(permission);
			server.setUpdated(new Date());
			server.setPluginVersion(version);
			serverDAO.save(server);
		} catch (UnauthorizedUpdateException e) {
			try {
				MailManager.getInstance().sendAdminMail("WARN", e.getMessage());
			} catch (Exception me) {
				log.error(me.getMessage());
			}
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param name
	 * @param version
	 * @param remoteAddr
	 */
	public void updateName(String key, String name, String version,	String remoteAddr) {
		updateName(key, name, version, 0, remoteAddr);
	}

	public void cleanServer(Server server) {
		cleanServer(server, true);
	}

	public void cleanServer(Server server, boolean updateDate) {
		playerDAO.cleanConnected(server.getKey());
		server.setOnlinePlayers(0);
		server.setDirty(false);
		if (updateDate) {
			server.setUpdated(new Date());	
		}
		serverDAO.save(server);
	}
	
	/**
	 * Update player info
	 * 
	 * @param server
	 *            the server instance
	 * @param list
	 *            a list of jipdbs.bean.PlayerInfo
	 * @throws Exception 
	 * @since 0.5
	 */
	public void updatePlayer(Server server, List<PlayerInfo> list) throws Exception {
		
		log.info("Processing server: " + server.getName());
		
		try {
			for (PlayerInfo playerInfo : list) {
				try {
					Date playerLastUpdate;
					Player player = playerDAO.findByServerAndGuid(server.getKey(),
							playerInfo.getGuid());
					if (player == null) {
						player = new Player();
						player.setCreated(new Date());
						player.setGuid(playerInfo.getGuid());
						player.setLevel(playerInfo.getLevel());
						player.setClientId(playerInfo.getClientId());
						player.setServer(server.getKey());
						player.setBanInfo(null);
						playerLastUpdate = playerInfo.getUpdated();
					} else {
						player.setClientId(playerInfo.getClientId());
						player.setLevel(playerInfo.getLevel());
						if (player.getClientId() == null || player.getClientId() == 0) {
							player.setClientId(playerInfo.getClientId());
						}
						playerLastUpdate = player.getUpdated();
					}
					player.setNickname(playerInfo.getName());
					player.setIp(playerInfo.getIp());
					if (player.getUpdated() == null || playerInfo.getUpdated().after(player.getUpdated())) {
						player.setUpdated(playerInfo.getUpdated());	
					}
					
					handlePlayerEvent(playerInfo, player);
					
					playerDAO.save(player);

					handlePenaltyEvent(playerInfo, player);
					
					boolean update = false;
					if (Events.CONNECT.equals(playerInfo.getEvent())) {
						if (server.getUpdated() == null
								|| server.getUpdated().after(playerLastUpdate)) {
							update = true;
						}
					}
					AliasManager.createAlias(player, update);

				} catch (Exception e) {
					log.error(e.getMessage());
					StringWriter w = new StringWriter();
					e.printStackTrace(new PrintWriter(w));
					log.error(w.getBuffer().toString());
				}
			}
			server.setDirty(true);
			server.setUpdated(new Date());
			serverDAO.save(server);
		} catch (Exception e) {
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw e;
		}
	}

	private void handlePenaltyEvent(PlayerInfo playerInfo, Player player) {
		if (Events.BAN.equals(playerInfo.getEvent())
			|| Events.ADDNOTE.equals(playerInfo.getEvent())
			|| Events.UNBAN.equals(playerInfo.getEvent())
			|| Events.DELNOTE.equals(playerInfo.getEvent())) {
			TaskManager.getInstance().runTask(new PenaltyTask(player, playerInfo.getEvent()));
		}
	}

	/**
	 * 
	 * @param playerInfo
	 * @param player
	 */
	private void handlePlayerEvent(PlayerInfo playerInfo, Player player) {
		if (Events.BAN.equals(playerInfo.getEvent())) {
			player.setBanInfo(playerInfo.getPenaltyInfo().getCreated());
			player.setConnected(false);
		} else if (Events.CONNECT.equals(playerInfo.getEvent())
				|| Events.DISCONNECT.equals(playerInfo.getEvent())
				|| Events.UNBAN.equals(playerInfo.getEvent())
				|| Events.UPDATE.equals(playerInfo.getEvent())) {
			player.setBanInfo(null);
			if (Events.CONNECT.equals(playerInfo.getEvent()) || Events.UPDATE.equals(playerInfo.getEvent())) {
				player.setConnected(true);
			} else if (Events.DISCONNECT.equals(playerInfo.getEvent())) {
				player.setConnected(false);
			}
		} else if (Events.ADDNOTE.equals(playerInfo.getEvent())) {
			player.setNote(playerInfo.getPenaltyInfo().getRawData());
		} else if (Events.DELNOTE.equals(playerInfo.getEvent())) {
			player.setNote(null);
		}
	}

}
