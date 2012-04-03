/**
 *   Copyright(c) 2010-2012 CodWar Soft
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
package iddb.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;

public final class GeoIpUtil {

	private static final Logger logger = LoggerFactory.getLogger(GeoIpUtil.class);
	
	private static GeoIpUtil instance;
	private LookupService service;
	
	private GeoIpUtil() {
		try {
			Properties props = new Properties();
			props.load(getClass().getClassLoader().getResourceAsStream("jipdbs.properties"));
			if (props.containsKey("geoipdat")) {
				service = new LookupService(props.getProperty("geoipdat"), LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);	
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static GeoIpUtil getInstance() {
		if (instance == null) {
			instance = new GeoIpUtil();
		}
		return instance;
	}
	
	public Country countryLookup(String ip) {
		if (this.service != null) {
			return this.service.getCountry(ip);
		}
		return null;
	}

}
