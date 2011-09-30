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
package iddb.web.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecurityConfig {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	private static SecurityConfig instance;
	private Map<String, Map<String, String>> config = new HashMap<String, Map<String,String>>();
	
	private SecurityConfig() {
		try {
			HierarchicalINIConfiguration configFile = new HierarchicalINIConfiguration(this.getClass().getClassLoader().getResource("security.properties"));
			for (Object section : configFile.getSections() ) {
				SubnodeConfiguration node = configFile.getSection((String) section);
				Map<String, String> map = new LinkedHashMap<String, String>();
				for (@SuppressWarnings("unchecked")
				Iterator<Object> it = node.getKeys(); it.hasNext() ; ) {
					String key = it.next().toString();
					if (log.isTraceEnabled()) {
						log.trace("Loading '{}' with value '{}' on section '{}'", new String[] {key, node.getString(key), section.toString()});
					}
					map.put(key, node.getString(key));
				}
				config.put(section.toString(), map);
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage());
		}
	}
	
	public static SecurityConfig getInstance() {
		if (instance == null) {
			instance = new SecurityConfig();
		}
		return instance;
	}

	public String getValue(String section, String key) {
		String value = null;
		if (config.containsKey(section)) {
			value = config.get(section).get(key);
		}
		return value;
	}
	
	public Map<String, String> getSection(String section) {
		return config.get(section);
	}
	
}
