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

import iddb.api.ServerManager;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.User;
import iddb.core.model.UserServer;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.core.model.dao.UserDAO;
import iddb.core.model.dao.UserServerDAO;
import iddb.core.util.MailManager;
import iddb.core.util.PasswordUtils;
import iddb.core.util.Validator;
import iddb.exception.EntityDoesNotExistsException;
import iddb.exception.UnauthorizedUpdateException;
import iddb.exception.UpdateApiException;
import iddb.info.PlayerInfo;
import iddb.task.TaskManager;
import iddb.task.tasks.UpdateTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Update {

	private static final Logger log = LoggerFactory.getLogger(Update.class); 

	protected final ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
	protected final AliasDAO aliasDAO = (AliasDAO) DAOFactory.forClass(AliasDAO.class);
	protected final PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);
	
	/**
	 * Updates the name of a server given its uid.
	 * <p>
	 * Invoked by the servers when they change their public server name.
	 * 
	 * @param key
	 *          the server uid.
	 * @param name
	 *          the server's new name.
	 * @param version
	 * 			plugin version number
	 * @param permission
	 * 			allowed actions for server admins
	 * @param remoteAddr
	 *            the server's remote address.
	 * @since 0.5
	 */
	public void updateName(String key, String name, String version,	Integer permission, String remoteAddr) {
		updateName(key, name, version, permission, 40, remoteAddr);
	}

	/**
	 * Updates the name of a server given its uid.
	 * <p>
	 * Invoked by the servers when they change their public server name.
	 * 
	 * @param key
	 *          the server uid.
	 * @param name
	 *          the server's new name.
	 * @param version
	 * 			plugin version number
	 * @param permission
	 * 			allowed actions for server admins
	 * @param adminLevel
	 * 			minimum level for remote actions  
	 * @param remoteAddr
	 *            the server's remote address.
	 * @since 0.8
	 */
	public void updateName(String key, String name, String version,	Integer permission, Integer adminLevel, String remoteAddr) {
		try {
			Server server = ServerManager.getAuthorizedServer(key, remoteAddr,name);
			server.setName(name);
			server.setPermission(permission);
			server.setUpdated(new Date());
			server.setPluginVersion(version);
			server.setAdminLevel(adminLevel);
			serverDAO.save(server);
		} catch (UnauthorizedUpdateException e) {
			try {
				MailManager.getInstance().sendAdminMail("WARN", e.getMessage());
			} catch (Exception me) {
				log.error(me.getMessage());
			}
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
		}
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
		log.info("Processing server: {}", server.getName());
		TaskManager.getInstance().runTask(new UpdateTask(server, list));
	}

	/**
	 * @param server
	 * @param userid
	 * @param playerInfo
	 */
	public Integer linkUser(Server server, String userid, PlayerInfo playerInfo) throws UpdateApiException, Exception {
		log.info("Linking player {} with user {}", playerInfo.getName(), userid);
		
		Player player = playerDAO.findByServerAndHash(server.getKey(), playerInfo.getHash());
		if (player == null) {
			log.debug("Player is unsynced.");
			return 1;
		}
		
		if (!Validator.isValidEmail(userid)) {
			log.debug("Invalid userid "+ userid);
			return 2;
		}
		
		UserServerDAO serverDAO = (UserServerDAO) DAOFactory.forClass(UserServerDAO.class);
		try {
			serverDAO.findByPlayerAndServer(player.getKey(), server.getKey());
			// already linked
			return 3;
		} catch (EntityDoesNotExistsException e) {
			// do nothing
		}
		
		UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
		User user;
		String password = null;
		try {
			user = userDAO.get(userid);
		} catch (EntityDoesNotExistsException e) {
			password = PasswordUtils.getRandomString();
			user = new User();
			user.setPassword(PasswordUtils.hashPassword(password));
			user.setLoginId(userid);
			user.setRoles(new HashSet<String>(Arrays.asList(new String[]{"user"})));
			userDAO.save(user);
		}
		UserServer userServer;
		try {
			userServer = serverDAO.findByUserAndServer(user.getKey(), server.getKey());
		} catch (EntityDoesNotExistsException e) {
			userServer = new UserServer();
			userServer.setUser(user.getKey());
			userServer.setServer(server.getKey());
			userServer.setOwner(false);
		}
		userServer.setPlayer(player.getKey());
		serverDAO.save(userServer);

		if (password != null) {
			try {
				Map<String, String> pw = new HashMap<String, String>();
				pw.put("password", password);
				MailManager manager = MailManager.getInstance();
				manager.sendMail("Nuevo usuario", "newuser", new String[] { userid }, pw);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return 0;
	}

}
