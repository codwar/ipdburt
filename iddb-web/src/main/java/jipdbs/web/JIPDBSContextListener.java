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
package jipdbs.web;

import iddb.core.JIPDBS;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JIPDBSContextListener implements ServletContextListener {

	private static Logger log = LoggerFactory.getLogger(JIPDBSContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.debug("Context destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Initializing listener");
		ServletContext context = event.getServletContext();

		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("jipdbs.properties"));
		} catch (Exception e) {
			log.warn("Unable to load context properties: ", e.getMessage());
		}
		context.setAttribute(Context.JIPDBS, new JIPDBS(props));
	}
}
