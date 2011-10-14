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

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CleanOnlinePlayersWorker extends TimerTask {

	@Override
	public void run() {
		ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);
		List<Server> servers = serverDAO.listNotUpdatedSince(new Date(new Date().getTime()-3600000)); // 1 hora
		for (Server server : servers) {
			if (server.getOnlinePlayers()>0) {
				Update api = new Update();
				api.cleanServer(server, false);
			}
		}
	}

}
