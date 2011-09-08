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
package iddb.task;

import iddb.core.model.Player;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PlayerDAO;
import iddb.exception.EntityDoesNotExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {

	private static final Logger log = LoggerFactory
			.getLogger(TaskManager.class);

	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory
			.forClass(PlayerDAO.class);

	private PenaltyTask penaltyTask = new PenaltyTask();

	public void processPenalty(String key, String event) {
		try {
			// TODO manejar keys
			Player player = playerDAO.get(Long.parseLong(key));
			penaltyTask.process(player, event);
		} catch (EntityDoesNotExistsException e) {
			log.error(e.getMessage());
		}
	}

}
