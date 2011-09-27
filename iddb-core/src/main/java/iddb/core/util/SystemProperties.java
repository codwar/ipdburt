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
package iddb.core.util;

import java.util.Properties;

public class SystemProperties {

	public static class ApplicationVersion {
		
		private String version;
		private String build;

		public ApplicationVersion(String v, String b) {
			version = v;
			build = b;
		}
		public String getVersion() {
			return version;
		}
		public String getBuild() {
			return build;
		}
		
	}
	
	private static SystemProperties instance;
	private ApplicationVersion version;
	
	private SystemProperties() {
	}
	
	private static SystemProperties getInstance() {
		if (instance == null) {
			instance = new SystemProperties();
		}
		return instance;
	}
	
	private ApplicationVersion getVersion() {
		if (version == null) {
	     	try {
	         	Properties prop = new Properties();
	         	prop.load(Functions.class.getClassLoader().getResourceAsStream("release.properties"));
	         	version = new ApplicationVersion(prop.getProperty("version"), prop.getProperty("build"));
	     	} catch (Exception e) {
	     		version = new ApplicationVersion("0", "0");
	     	}			
		}
		return version;
	}
	
	public static ApplicationVersion applicationVersion() {
		return SystemProperties.getInstance().getVersion();
	}
}
