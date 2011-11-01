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

import iddb.core.model.Penalty;
import iddb.core.model.PenaltyHistory;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PenaltyHistoryDAO;
import iddb.exception.EntityDoesNotExistsException;
import iddb.scheduller.Worker;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyEventWorker implements Worker {

	private static final Logger log = LoggerFactory.getLogger(PenaltyEventWorker.class);
	private final Integer THRESHOLD = 24;
	
	@Override
	public void execute() {
		Date grace = DateUtils.addHours(new Date(), THRESHOLD * -1);
		
		PenaltyDAO penaltyDAO = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);
		PenaltyHistoryDAO historyDAO = (PenaltyHistoryDAO) DAOFactory.forClass(PenaltyHistoryDAO.class);
		
		List<PenaltyHistory> penalties = historyDAO.listByStatus(PenaltyHistory.ST_WAITING);
		
		int c = 0;
		for (PenaltyHistory p : penalties) {
			if (p.getUpdated().before(grace)) {
				try {
					Penalty penalty = penaltyDAO.get(p.getPenaltyId());
					penalty.setSynced(false);
					p.setStatus(PenaltyHistory.ST_PENDING);
					penaltyDAO.save(penalty);
					historyDAO.save(p);
					c++;
				} catch (EntityDoesNotExistsException e) {
					log.error(e.getMessage());
				}
			}
		}
		log.debug("Updated {}", c);
		
	}

}
