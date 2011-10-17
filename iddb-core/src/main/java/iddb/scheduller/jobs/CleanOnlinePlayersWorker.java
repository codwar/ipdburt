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
package iddb.scheduller.jobs;

import iddb.api.v2.Update;
import iddb.core.model.Server;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.ServerDAO;
import iddb.scheduller.Worker;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanOnlinePlayersWorker implements Worker {

	private static final Logger log = LoggerFactory.getLogger(CleanOnlinePlayersWorker.class);
	
	@Override
	public void execute() {
		ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
		List<Server> servers = serverDAO.listNotUpdatedSince(new Date(new Date().getTime()-3600000)); // 1 hora
		int c = 0;
		Update api = new Update();
		for (Server server : servers) {
			if (server.getOnlinePlayers()>0) {
				c++;
				api.cleanServer(server, false);
			}
		}
		log.info("Servers cleanded {}", c);
	}

}
