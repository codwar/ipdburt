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
package iddb.quartz.job;

import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.UserDAO;
import iddb.scheduller.Worker;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanUpPasswordKeys implements Job {

	private static final Logger log = LoggerFactory.getLogger(CleanUpPasswordKeys.class);
	
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug("Running password clean up");
		Worker worker = new Worker() {
			
			@Override
			public void execute() {
				UserDAO userDAO = (UserDAO) DAOFactory.forClass(UserDAO.class);
				Integer res = userDAO.cleanUp(24);
				log.debug("Cleaned {} keys", res);
			}
		};
		worker.execute();
	}

}
