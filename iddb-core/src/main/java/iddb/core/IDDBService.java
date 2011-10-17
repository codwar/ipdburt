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
package iddb.core;

import iddb.core.model.Alias;
import iddb.core.model.AliasIP;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.AliasIPDAO;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.core.util.MailManager;
import iddb.exception.EntityDoesNotExistsException;
import iddb.info.AliasResult;
import iddb.info.SearchResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IDDBService {

	private static final Logger log = LoggerFactory.getLogger(IDDBService.class);

	protected final ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
	protected final AliasDAO aliasDAO = (AliasDAO) DAOFactory.forClass(AliasDAO.class);
	protected final AliasIPDAO aliasIpDAO = (AliasIPDAO) DAOFactory.forClass(AliasIPDAO.class);
	protected final PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);

	private final String recaptchaPublicKey;
	private final String recaptchaPrivateKey;

	public IDDBService() {
		this("","");
	}
	
	public IDDBService(String captchaPublicKey, String captchaPrivateKey) {
		recaptchaPublicKey = captchaPublicKey;
		recaptchaPrivateKey = captchaPrivateKey;
	}

	public String getNewRecaptchaCode() {
		if (StringUtils.isEmpty(recaptchaPrivateKey) || StringUtils.isEmpty(recaptchaPublicKey)) {
			log.warn("reCaptcha has not been initialized correclty.");
		}
		ReCaptcha c = ReCaptchaFactory.newReCaptcha(recaptchaPublicKey,
				recaptchaPrivateKey, false);
		Properties cProp = new Properties();
		cProp.setProperty("theme", "white");
		cProp.setProperty("lang", "es");
		return c.createRecaptchaHtml(null, cProp);
	}

	public boolean isRecaptchaValid(String remoteAddr, String challenge,
			String uresponse) {
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

		reCaptcha.setPrivateKey(recaptchaPrivateKey);

		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
				challenge, uresponse);

		return reCaptchaResponse.isValid();
	}

	public void addServer(String name, String admin, String uid, String ip, boolean disabled) {

		Server server = new Server();
		server.setAdminEmail(admin);
		server.setCreated(new Date());
		server.setUid(uid);
		server.setName(name);
		server.setOnlinePlayers(0);
		server.setAddress(ip);
		server.setDisabled(disabled);
		server.setPermission(0);
		serverDAO.save(server);
	}

	public Server getServer(String key) throws EntityDoesNotExistsException {
		return getServer(Long.parseLong(key));
	}

	public Server getServer(Long key) throws EntityDoesNotExistsException {
		return serverDAO.get(key);
	}

	public void saveServer(String key, String name, String admin, String ip, boolean disabled) {
		try {
			Server server = getServer(key);
			server.setName(name);
			server.setAdminEmail(admin);
			server.setAddress(ip);
			server.setDisabled(disabled);
			serverDAO.save(server);
		} catch (EntityDoesNotExistsException e) {
			log.error(e.toString());
		}
	}

	public List<Server> getServers() {
		int[] count = new int[1];
		return getServers(0, 1000, count);
	}

	public List<Server> getActiveServers(int offset, int limit, int[] count) {
		try {
			return serverDAO.findEnabled(offset, limit, count);
		} catch (Exception e) {
			log.error("Unable to fetch servers [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}
	
	public List<Server> getServers(int offset, int limit, int[] count) {
		try {
			return serverDAO.findAll(offset, limit, count);
		} catch (Exception e) {
			log.error("Unable to fetch servers [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> rootQuery(int offset, int limit, int[] count) {

		try {
			List<Player> players = playerDAO.findLatest(offset, limit, count);

			List<SearchResult> results = new ArrayList<SearchResult>();

			for (Player player : players) {
				Server server = serverDAO.get(player.getServer());

				// Whoops! inconsistent data.
				if (server == null)
					continue;

				SearchResult result = marshall(player, server);
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.error("Unable to fetch root query players [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> bannedQuery(int offset, int limit, int[] count) {

		try {
			List<Player> players = playerDAO.findBanned(offset, limit, count);

			List<SearchResult> results = new ArrayList<SearchResult>();

			for (Player player : players) {
				Server server = serverDAO.get(player.getServer());

				// Whoops! inconsistent data.
				if (server == null)
					continue;

				SearchResult result = marshall(player, server);
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.error("Unable to fetch banned query players: [{}]", e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.toString());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> aliasSearch(String query, int offset, int limit,
			int[] count, boolean exactMatch) {

		try {
			List<Alias> aliasses = new ArrayList<Alias>();

			if (exactMatch) {
				aliasses = aliasDAO.findByNickname(query, offset, limit < Parameters.MAX_SEARCH_LIMIT ? limit : Parameters.MAX_SEARCH_LIMIT, count);
			} else if (query.length() >= Parameters.MIN_NGRAM_QUERY && query.length() <= Parameters.MAX_NGRAM_QUERY) {
				query = query.length() <= Parameters.MAX_NGRAM_QUERY ? query : query.substring(0, Parameters.MAX_NGRAM_QUERY);
				aliasses = aliasDAO.findBySimilar(query, offset, limit < Parameters.MAX_SEARCH_LIMIT ? limit : Parameters.MAX_SEARCH_LIMIT, count);
			}

			return marshall(aliasses);
		} catch (Exception e) {
			log.error("Unable to fetch players: [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> ipSearch(String query, int offset, int limit,
			int[] count) {

		try {
			List<SearchResult> results = new ArrayList<SearchResult>();
			List<AliasIP> aliasses = aliasIpDAO.findByIP(query, offset, limit,
					count);

			for (AliasIP alias : aliasses) {
				Player player = playerDAO.get(alias.getPlayer());
				Server server = serverDAO.get(player.getServer());

				// Whoops! inconsistent data.
				if (alias == null || server == null)
					continue;

				SearchResult result = marshall(player, server);
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.error("Unable to fetch players [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> byServerSearch(Long serverId, int offset,
			int limit, int[] count) {

		try {
			List<SearchResult> results = new ArrayList<SearchResult>();

			Server server = serverDAO.get(serverId);
			if (server != null) {
				for (Player player : playerDAO.findByServer(serverId, offset,
						limit, count)) {
					results.add(marshall(player, server));
				}
			}
			return results;
		} catch (Exception e) {
			log.error("Unable to fetch players [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	private List<SearchResult> marshall(List<Alias> aliasses)
			throws EntityDoesNotExistsException {

		List<SearchResult> results = new ArrayList<SearchResult>();

		for (Alias alias : aliasses) {

			Player player = playerDAO.get(alias.getPlayer());
			Server server = serverDAO.get(player.getServer());

			// Whoops! inconsistent data.
			if (alias == null || server == null)
				continue;

			SearchResult result = marshall(player, server);
			results.add(result);
		}

		return results;
	}

	private SearchResult marshall(Player player, Server server) {
		SearchResult result = new SearchResult();
		result.setId(player.getKey());
		result.setKey(player.getKey());
		result.setIp(player.getIp());
		result.setLatest(player.getUpdated());
		result.setPlaying(player.isConnected());
		result.setNote(player.getNote() != null);
		result.setName(player.getNickname());
		result.setServer(server);
		result.setBanned(player.getBanInfo() != null);
		result.setClientId(player.getClientId() != null ? "@"
				+ player.getClientId().toString() : "UID" + result.getId());
		return result;
	}

	public Player getPlayer(Long player) throws EntityDoesNotExistsException {
		return playerDAO.get(player);
	}

	public Player getPlayer(String key) throws EntityDoesNotExistsException {
		try {
			Long id = Long.parseLong(key);
			return getPlayer(id);
		} catch (NumberFormatException e) {
			log.debug("Key {} is an old format", key);
			return playerDAO.findByOldKey(key);
		}
	}
	
	public List<AliasResult> alias(String key, int offset, int limit,
			int[] count) {

		try {
			List<AliasResult> result = new ArrayList<AliasResult>();

			Player player = playerDAO.get(Long.parseLong(key));

			if (player != null) {
				List<Alias> aliasses = aliasDAO.findByPlayer(player.getKey(),
						offset, limit, count);

				for (Alias alias : aliasses) {

					AliasResult item = new AliasResult();
					item.setCount(alias.getCount().intValue());
					item.setIp(null);
					item.setNickname(alias.getNickname());
					item.setUpdated(alias.getUpdated());
					result.add(item);
				}
			}

			return result;
		} catch (Exception e) {
			log.error("Unable to fetch aliasses [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<AliasResult> aliasip(String key, int offset, int limit,
			int[] count) {

		try {
			List<AliasResult> result = new ArrayList<AliasResult>();

			Player player = playerDAO.get(Long.parseLong(key));

			if (player != null) {
				List<AliasIP> aliasses = aliasIpDAO.findByPlayer(
						player.getKey(), offset, limit, count);

				for (AliasIP alias : aliasses) {
					AliasResult item = new AliasResult();
					item.setCount(alias.getCount().intValue());
					item.setIp(alias.getIp());
					item.setNickname(null);
					item.setUpdated(alias.getUpdated());
					result.add(item);
				}
			}

			return result;
		} catch (Exception e) {
			log.error("Unable to fetch IP aliasses [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public void sendAdminMail(String realId, String from, String body) {
		StringBuilder builder = new StringBuilder();
		builder.append("Responder a: ");
		builder.append(from);
		builder.append("\r\n");
		if (realId != null) {
			builder.append("Identificado como: ");
			builder.append(realId);
			builder.append("\r\n");
		}
		builder.append("\r\n");
		builder.append("------------- MENSAJE -------------\r\n\r\n");
		builder.append(body);
		
		try {
			MailManager.getInstance().sendAdminMail("Mensaje enviado desde IPDB", builder.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void refreshServerInfo(Server server) {
		int c = playerDAO.countConnected(server.getKey());
		server.setOnlinePlayers(c);
		server.setDirty(false);
		serverDAO.save(server);
	}

	public List<SearchResult> clientIdSearch(Long clientId, int offset,
			int limit, int[] count) {

		try {
			List<SearchResult> results = new ArrayList<SearchResult>();
			List<Player> players = playerDAO.findByClientId(clientId, offset,
					limit, count);

			for (Player player : players) {
				Server server = serverDAO.get(player.getServer());

				// Whoops! inconsistent data.
				if (server == null)
					continue;

				SearchResult result = marshall(player, server);
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.error("Unable to fetch players [{}]", e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public Penalty getLastPenalty(Long key) {
		return penaltyDAO.findLastActivePenalty(key, Penalty.BAN);
	}
	
	public Penalty getLastPenalty(Player player) {
		Penalty p = null;
		if (player.getBanInfo() != null) {
			p = getLastPenalty(player.getKey());
		}
		return p;
	}
	
	public Penalty getLastNotice(Player player) {
		Penalty p = null;
		if (player.getNote() != null) {
			p = penaltyDAO.findLastActivePenalty(player.getKey(), Penalty.NOTICE);
		}
		return p;
	}

}
