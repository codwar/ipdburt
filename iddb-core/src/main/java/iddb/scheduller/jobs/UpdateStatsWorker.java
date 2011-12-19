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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iddb.core.DAOException;
import iddb.core.model.Server;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.scheduller.Worker;

public class UpdateStatsWorker implements Worker {

	private static final Logger log = LoggerFactory.getLogger(UpdateStatsWorker.class);
	
	/* (non-Javadoc)
	 * @see iddb.scheduller.Worker#execute()
	 */
	@Override
	public void execute() {
		PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
		ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);

		int[] count = new int[1];
		List<Server> servers = serverDAO.findEnabled(0, 1000, count);
		
		for (Server server : servers) {
			int c = playerDAO.countByServer(server.getKey(), false);
			log.debug("Total players for server {}: {}", server.getName(), c);
			server.setTotalPlayers(c);
			try {
				serverDAO.save(server);
			} catch (DAOException e) {
			}
		}
		
	}

}
