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

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobManager {

	private static Logger log = LoggerFactory.getLogger(JobManager.class);
	
	private Map<JobDetail, Trigger> jobs;
	
	@SuppressWarnings("unchecked")
	public JobManager() {
		jobs = new HashMap<JobDetail, Trigger>();
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("cron.properties"));
			for (Entry<Object, Object> entry : props.entrySet()) {
				String jobname = (String) entry.getKey();
				String executionInfo = (String) entry.getValue();
				log.debug("Parsing {} using {}", jobname, executionInfo);
				Class<Job> jobClass;
				try {
					jobClass = (Class<Job>) Class.forName(jobname);
					JobDetail jobDetail = newJob(jobClass).withIdentity(jobname, jobname).build();
					Trigger trigger = newTrigger().withIdentity("trigger"+jobname, jobname).withSchedule(cronSchedule("0 " + executionInfo)).build();
					jobs.put(jobDetail, trigger);
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage());
				} catch (ParseException e) {
					log.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}
	
	public Map<JobDetail, Trigger> getJobs() {
		return jobs;
	}
	
	
	
}
