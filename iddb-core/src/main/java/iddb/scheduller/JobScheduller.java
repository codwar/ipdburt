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
package iddb.scheduller;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScheduller {

	private static final Logger log = LoggerFactory.getLogger(JobScheduller.class);
	
	public void setup() {
		Properties prop = new Properties();
		// TODO load cron.properties
		/*
		 * className=0 5 * * * (crontab format)
		 */
		
		for (Entry<Object, Object> entry : prop.entrySet()) {
			String jobClass = (String) entry.getKey();
			String timePattern = (String) entry.getValue();
			String[] tp = timePattern.split("\\s");
			if (tp.length == 5) {
				Timer timer = new Timer();
			} else {
				log.error("Invalid time entry {}", timePattern);
			}
		}
		
	}
}
