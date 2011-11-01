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

import iddb.core.model.Penalty;
import iddb.core.model.PenaltyHistory;
import iddb.core.model.Player;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PenaltyHistoryDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmRemoteEventTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ConfirmRemoteEventTask.class);

	protected final PenaltyHistoryDAO penaltyHistoryDAO = (PenaltyHistoryDAO) DAOFactory.forClass(PenaltyHistoryDAO.class);
	protected final PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);
	protected final PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);

	private List<Entry<Long, String>> entries;
	
	public ConfirmRemoteEventTask(List<Entry<Long, String>> entries) {
		this.entries = entries;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			updateEvent();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void updateEvent() {
		for (Entry<Long, String> entry : this.entries) {
			PenaltyHistory his = null;
			try {
				Long eventId = entry.getKey();
				String msg = entry.getValue();
				his = penaltyHistoryDAO.get(eventId);
				Penalty penalty = null;
				Player player = null;
				log.debug("Confirm event {} {}", eventId, msg);
				if (his.getStatus() != PenaltyHistory.ST_WAITING) {
					log.warn("Event {} already confirmed", eventId);
					continue;
				}
				if (msg == null || "".equals(msg)) {
					his.setStatus(PenaltyHistory.ST_DONE);
					penalty = penaltyDAO.get(his.getPenaltyId());
					penalty.setSynced(true);
					if (penalty.getType() == Penalty.BAN) {
						player = playerDAO.get(penalty.getPlayer());
					}
					if (his.getFuncId() == PenaltyHistory.FUNC_ID_ADD) {
						if (player != null) {
							player.setBanInfo(penalty.getCreated());
						}
						penalty.setActive(true);
					} else {
						if (player != null) {
							player.setBanInfo(null);
						}						
						penalty.setActive(false);
					}
					if (player != null) playerDAO.save(player);
					penaltyDAO.save(penalty);
				} else {
					his.setStatus(PenaltyHistory.ST_ERROR);
					his.setError(msg);
				}
				his.setUpdated(new Date());
				penaltyHistoryDAO.save(his);
			} catch (EntityDoesNotExistsException e) {
				log.error(e.getMessage());
				if (his != null) {
					try {
						his.setStatus(PenaltyHistory.ST_ERROR);
						his.setError(e.getMessage());
						penaltyHistoryDAO.save(his);
					} catch (Exception e1) {
						log.error(e1.getMessage());
					}
				}
			}
		}
	}

}
