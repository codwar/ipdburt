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
package iddb.task.tasks;

import iddb.api.Events;
import iddb.core.PenaltyService;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.core.model.util.AliasManager;
import iddb.exception.EntityDoesNotExistsException;
import iddb.info.PlayerInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sgt.utils.sql.builder.QueryBuilder;

public class UpdateTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UpdateTask.class);

	public static final int GRACE_PERIOD = 15;

	protected final ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
	protected final AliasDAO aliasDAO = (AliasDAO) DAOFactory.forClass(AliasDAO.class);
	protected final PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);

	private Server server;
	private List<PlayerInfo> playerList;
	
	public UpdateTask(Server server, List<PlayerInfo> list) {
		this.server = server;
		this.playerList = list;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			updatePlayer(server, playerList);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public Player createPlayer(Server server, PlayerInfo playerInfo) {
		Player player = new Player();
		player.setCreated(new Date());
		player.setGuid(playerInfo.getGuid());
		player.setHash(playerInfo.getHash());
		player.setLevel(playerInfo.getLevel());
		player.setClientId(playerInfo.getClientId());
		player.setServer(server.getKey());
		player.setBanInfo(null);
		player.setNickname(playerInfo.getName());
		player.setIp(playerInfo.getIp());
		player.setPbid(playerInfo.getPbid());
		return player;
	}
	
	public void updatePlayer(Server server, List<PlayerInfo> list) throws Exception {
		try {
			for (PlayerInfo playerInfo : list) {
				try {
					Date playerLastUpdate;
					boolean created = false;
					Player player = playerDAO.findByServerAndHash(server.getKey(),
							playerInfo.getHash());
					if (player == null) {
						created = true;
						player = createPlayer(server, playerInfo);
						playerLastUpdate = playerInfo.getUpdated();
					} else {
						if (StringUtils.isEmpty(player.getGuid()) && StringUtils.isNotEmpty(playerInfo.getGuid())) {
							player.setGuid(playerInfo.getGuid());
						}
						player.setNickname(playerInfo.getName());
						player.setIp(playerInfo.getIp());
						player.setClientId(playerInfo.getClientId());
						player.setLevel(playerInfo.getLevel());
						if (player.getClientId() == null || player.getClientId() == 0) {
							player.setClientId(playerInfo.getClientId());
						}
						playerLastUpdate = player.getUpdated();
					}
					if (player.getUpdated() == null || playerInfo.getUpdated().after(player.getUpdated())) {
						player.setUpdated(playerInfo.getUpdated());	
					}
					
					handlePlayerEvent(playerInfo, player);
					
					if (created) playerDAO.save(player);

					if (handlePenaltyEvent(playerInfo, player) || !created) {
						playerDAO.save(player);
					}
					
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

	private boolean handlePenaltyEvent(PlayerInfo playerInfo, Player player) {
		boolean updated = false;
		PenaltyService penaltyService = new PenaltyService();
		
		if (Events.BAN.equals(playerInfo.getEvent())
			|| Events.ADDNOTE.equals(playerInfo.getEvent())
			|| Events.UNBAN.equals(playerInfo.getEvent())
			|| Events.DELNOTE.equals(playerInfo.getEvent())) {
			
			if (Events.UNBAN.equals(playerInfo.getEvent())) {
				penaltyDAO.deletePlayerPenalty(player.getKey(), Penalty.BAN);
				player.setBanInfo(null);
			} else if (Events.BAN.equals(playerInfo.getEvent())) {
				Penalty penalty = new Penalty();
				penalty.setPlayer(player.getKey());
				penalty.setType(Penalty.BAN);
				penalty.setCreated(playerInfo.getPenaltyInfo().getCreated());
				penalty.setUpdated(new Date());
				penalty.setReason(playerInfo.getPenaltyInfo().getReason());
				penalty.setDuration(playerInfo.getPenaltyInfo().getDuration());
				penalty.setSynced(true);
				penalty.setActive(true);
				if (StringUtils.isNotEmpty(playerInfo.getPenaltyInfo().getAdminId())) {
					try {
						Player admin = playerDAO.findByServerAndHash(player.getServer(), playerInfo.getPenaltyInfo().getAdminId());
						if (admin != null) penalty.setAdmin(admin.getKey());
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				penaltyDAO.deletePlayerPenalty(player.getKey(), Penalty.BAN);
				penaltyService.addPenalty(penalty, player);
				updated = true;
			} else if (Events.ADDNOTE.equals(playerInfo.getEvent())) {
				Penalty penalty = new Penalty();
				penalty.setPlayer(player.getKey());
				penalty.setType(Penalty.NOTICE);
				penalty.setCreated(playerInfo.getPenaltyInfo().getCreated());
				penalty.setUpdated(new Date());
				penalty.setReason(playerInfo.getPenaltyInfo().getReason());
				penalty.setSynced(true);
				penalty.setActive(true);
				if (playerInfo.getPenaltyInfo().getRemoteId() != null) {
					if (playerInfo.getPenaltyInfo().getRemoteId().startsWith("P$")) {
						penalty.setDuration(Long.parseLong(playerInfo.getPenaltyInfo().getRemoteId().substring(2)));
					}
				}
				if (StringUtils.isNotEmpty(playerInfo.getPenaltyInfo().getAdminId())) {
					try {
						Player admin = playerDAO.findByServerAndHash(player.getServer(), playerInfo.getPenaltyInfo().getAdminId());
						if (admin != null) penalty.setAdmin(admin.getKey());
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				penaltyService.addPenalty(penalty, player);
				updated = true;
			} else if (Events.DELNOTE.equals(playerInfo.getEvent())) {
				try {
					if (playerInfo.getPenaltyInfo().getRemoteId() != null) {
						if (playerInfo.getPenaltyInfo().getRemoteId().startsWith("P$")) {
							Long id = Long.parseLong(playerInfo.getPenaltyInfo().getRemoteId().substring(2));
							QueryBuilder b = new QueryBuilder();
							b.and("playerid", player.getKey());
							b.and("type", Penalty.NOTICE);
							b.and("duration", id);
							penaltyDAO.delete(penaltyDAO.query(b.toString()));
						} else {
							Long id = Long.parseLong(playerInfo.getPenaltyInfo().getRemoteId());
							try {
								Penalty p = penaltyDAO.get(id);
								penaltyDAO.delete(p);
							} catch (EntityDoesNotExistsException e) {
								log.warn(e.getMessage());
							}
						}
					}
				} catch (NumberFormatException e) {
					log.warn(e.getMessage());
				}
			} else {
				log.warn("Unhandled event {}", playerInfo.getEvent());
			}
		}
		return updated;
	}

	/**
	 * 
	 * @param playerInfo
	 * @param player
	 */
	private void handlePlayerEvent(PlayerInfo playerInfo, Player player) {
		Date grace = DateUtils.addMinutes(new Date(), GRACE_PERIOD * -1);
		if (Events.BAN.equals(playerInfo.getEvent())) {
			player.setBanInfo(playerInfo.getPenaltyInfo().getCreated());
			player.setConnected(false);
		} else if (Events.CONNECT.equals(playerInfo.getEvent())
				|| Events.DISCONNECT.equals(playerInfo.getEvent())
				|| Events.UNBAN.equals(playerInfo.getEvent())
				|| Events.UPDATE.equals(playerInfo.getEvent())) {
			//player.setBanInfo(null);
			if (playerInfo.getUpdated().after(grace) && 
					(Events.CONNECT.equals(playerInfo.getEvent()) 
							|| Events.UPDATE.equals(playerInfo.getEvent()))) {
				player.setConnected(true);
			} else {
				player.setConnected(false);
			}
		} else if (Events.ADDNOTE.equals(playerInfo.getEvent())) {
			player.setNote(playerInfo.getPenaltyInfo().getCreated());
		} else if (Events.DELNOTE.equals(playerInfo.getEvent())) {
			player.setNote(null);
		}
	}	
}
