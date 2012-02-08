/**
 *   Copyright(c) 2010-2011-2012 CodWar Soft
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

import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PenaltyHistoryDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyService {

	private static final Logger logger = LoggerFactory.getLogger(PenaltyService.class);

	protected final PenaltyHistoryDAO penaltyHistoryDAO = (PenaltyHistoryDAO) DAOFactory.forClass(PenaltyHistoryDAO.class);
	protected final PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);

	public void addPenalty(Penalty penalty, Player player) {
		if (Penalty.BAN == penalty.getType()) {
			player.setBanInfo(penalty.getCreated());
		}
		penalty.setActive(true);
		penaltyDAO.save(penalty);
		checkNoticeStatus(penalty, player);
	}
	
	public void removePenalty(Penalty penalty, Player player) {
		if (Penalty.BAN == penalty.getType()) {
			player.setBanInfo(null);
		}
		penalty.setActive(false);
		penaltyDAO.save(penalty);
		checkNoticeStatus(penalty, player);
	}
	
	public void checkNoticeStatus(Penalty penalty, Player player) {
		if (Penalty.NOTICE == penalty.getType()) {
			Penalty note = penaltyDAO.findLastActivePenalty(player.getKey(), Penalty.NOTICE);
			if (note == null) {
				player.setNote(null);
			} else {
				player.setNote(note.getCreated());
			}
		}
	}
	
}
