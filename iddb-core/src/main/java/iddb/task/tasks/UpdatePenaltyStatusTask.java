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

import iddb.core.model.PenaltyHistory;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyHistoryDAO;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePenaltyStatusTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UpdatePenaltyStatusTask.class);

	protected final PenaltyHistoryDAO penaltyDAO = (PenaltyHistoryDAO) DAOFactory.forClass(PenaltyHistoryDAO.class);

	private List<PenaltyHistory> penaltyList;
	private Integer status;
	
	public UpdatePenaltyStatusTask(List<PenaltyHistory> list, Integer status) {
		this.penaltyList = list;
		this.status = status;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			updatePenalty();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void updatePenalty() {
		List<Long> ids = new ArrayList<Long>();
		for (PenaltyHistory his : this.penaltyList) {
			ids.add(his.getKey());
		}
		penaltyDAO.updateStatus(ids, this.status);
	}

}
