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
package iddb.web.security.service;

import iddb.web.security.SecurityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceFactory {

	private static final Logger log = LoggerFactory.getLogger(UserServiceFactory.class);
	
	private static UserServiceFactory instance;
	private UserService userService;
	
	private UserServiceFactory() {
		log.debug("Initializing UserServiceFactory");
		//Properties prop = new Properties();
		try {
			//prop.load(this.getClass().getClassLoader().getResourceAsStream("security.properties"));
			//String us = prop.getProperty("userService");
			String us = SecurityConfig.getInstance().getValue("service", "userService");
			log.trace("Create new instance of {}", us);
			@SuppressWarnings({ "static-access", "rawtypes" })
			Class cls = this.getClass().forName(us);
			this.userService = (UserService) cls.newInstance();
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	public static UserService getUserService() {
		if (instance == null) {
			instance = new UserServiceFactory();
		}
		return instance.userService;
	}
	
}
