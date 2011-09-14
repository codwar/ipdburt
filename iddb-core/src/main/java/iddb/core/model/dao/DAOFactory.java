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
package iddb.core.model.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DAOFactory {

	private static DAOFactory instance;
	private static final Logger log = LoggerFactory.getLogger(DAOFactory.class);
	
	private Map<String, Object> daoCache = new HashMap<String, Object>();

	private DAOFactory() {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream("dao.properties"));
			for (Entry<Object, Object> entry : prop.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				log.debug("Initializing {}", value);
				try {
					@SuppressWarnings({ "static-access", "rawtypes" })
					Class cls = this.getClass().forName(value);
					daoCache.put(key, cls.newInstance());
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage());
				} catch (InstantiationException e) {
					log.error(e.getMessage());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public Map<String, Object> getDaoCache() {
		return daoCache;
	}

	@SuppressWarnings("rawtypes")
	public static Object forClass(Class claz) {
		if (instance == null) {
			instance = new DAOFactory();
		}
		Object dao = instance.getDaoCache().get(claz.getName());
		if (dao == null) {
			log.error("There is no DAO associated with {}", claz.getName());
		}
		return dao;
	}
	
}
