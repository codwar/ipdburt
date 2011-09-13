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

import java.util.HashMap;
import java.util.Map;

public final class DAOFactory {

	private static Map<String, Object> daoCache = new HashMap<String, Object>();
	
	@SuppressWarnings("rawtypes")
	public static Object forClass(Class claz) {
		if (daoCache.containsKey(claz.getName())) {
			return daoCache.get(claz.getName());
		}
		return null;
	}
	
	public static ServerDAO getServerDAO() {
		return (ServerDAO) forClass(ServerDAO.class);
	}
	
}
