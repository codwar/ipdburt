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

import iddb.core.cache.CacheFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DAOFactory {

	private static DAOFactory instance;
	private static final Logger log = LoggerFactory.getLogger(DAOFactory.class);

	private static final String PACKAGE = "iddb.runtime.db.model.dao.impl.";
	private Boolean useCache = false;
	private String engine;
	
	private Map<String, Object> daoCache = new HashMap<String, Object>();

	private DAOFactory() {
		// let see if we have a cache system available
//		boolean useCache = false;
//		if (CacheFactory.getInstance().verify()) {
//			log.debug("Cache system is available, will try to create cached DAO");
//			useCache = true;
//		}
//		Properties prop = new Properties();
//		try {
//			prop.load(this.getClass().getClassLoader()
//					.getResourceAsStream("dao.properties"));
//			for (Entry<Object, Object> entry : prop.entrySet()) {
//				String key = (String) entry.getKey();
//				String value = (String) entry.getValue();
//				log.debug("Initializing {}", value);
//				try {
//					@SuppressWarnings({ "static-access", "rawtypes" })
//					Class cls = this.getClass().forName(value);
//					Object daoImpl = cls.newInstance();
//					if (useCache) {
//						try {
//							daoCache.put(key,
//									createCachedInstance(key, daoImpl));
//						} catch (Exception e) {
//							log.error(e.getMessage());
//							daoCache.put(key, daoImpl);
//						}
//					} else {
//						daoCache.put(key, daoImpl);
//					}
//				} catch (ClassNotFoundException e) {
//					log.error("{} not found", e.getMessage());
//				} catch (InstantiationException e) {
//					log.error("Cannot initialize {}", e.getMessage());
//				} catch (IllegalAccessException e) {
//					log.error(e.getMessage());
//				}
//			}
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
		
		if (CacheFactory.getInstance().verify()) {
			log.debug("Cache system is available, will try to create cached DAO");
			this.useCache = true;
		}
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream("db.properties"));
			this.engine = prop.getProperty("engine");
			log.debug("Using engine {}", this.engine);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object createCachedInstance(String iface, Object impl)
			throws Exception {
		String[] ifacePart = StringUtils.split(iface, ".");
		String ifaceName = ifacePart[ifacePart.length - 1];
		log.debug("Getting cached instance for {} - {}", iface, ifaceName);
		ClassLoader loader = this.getClass().getClassLoader();

		try {
			Class clz = loader.loadClass("iddb.core.model.dao.cached." + ifaceName + "Cached");
			Constructor cons = clz.getConstructor(new Class[] { Class.forName(iface) });
			return cons.newInstance(impl);
		} catch (SecurityException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (ClassNotFoundException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (NoSuchMethodException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (InstantiationException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			log.warn("No cached implementation found for {} - {}", ifaceName,
					e.getMessage());
			throw new Exception(e);
		}

	}

	public Map<String, Object> getDaoCache() {
		return daoCache;
	}

	public Object getDaoImpl(String name) {
		Object dao = getDaoCache().get(name);
		if (dao == null) {
			String[] ifacePart = StringUtils.split(name, ".");
			String ifaceName = ifacePart[ifacePart.length - 1];
			String classname = PACKAGE + this.engine + "." + ifaceName + "Impl";
			log.debug("Initializing {}", classname);
			try {
				@SuppressWarnings({ "static-access", "rawtypes" })
				Class cls = this.getClass().forName(classname);
				Object daoImpl = cls.newInstance();
				if (useCache) {
					try {
						dao = createCachedInstance(name, daoImpl);
					} catch (Exception e) {
						log.error(e.getMessage());
						dao = daoImpl;
					}
				} else {
					dao = daoImpl;
				}
				daoCache.put(name, dao);
			} catch (ClassNotFoundException e) {
				log.error("{} not found", e.getMessage());
			} catch (InstantiationException e) {
				log.error("Cannot initialize {}", e.getMessage());
			} catch (IllegalAccessException e) {
				log.error(e.getMessage());
			}
		}
		return dao;
	}
	@SuppressWarnings("rawtypes")
	public static Object forClass(Class claz) {
		if (instance == null) {
			instance = new DAOFactory();
		}
		Object dao = instance.getDaoImpl(claz.getName());
		if (dao == null) {
			log.debug("Failed lookup dao for {}", claz.getName());
		}
		return dao;
	}

}
