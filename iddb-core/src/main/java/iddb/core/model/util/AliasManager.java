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
package iddb.core.model.util;

import iddb.core.model.Alias;
import iddb.core.model.AliasIP;
import iddb.core.model.Player;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.AliasIPDAO;
import iddb.core.model.dao.DAOFactory;

import java.util.Date;

public class AliasManager {

	private static AliasDAO aliasDAO = (AliasDAO) DAOFactory.forClass(AliasDAO.class);
	private static AliasIPDAO ipDAO = (AliasIPDAO) DAOFactory.forClass(AliasIPDAO.class);
	
	private AliasManager() {};
	
	public static Alias createAlias(Player player, boolean update) {
		Alias alias = aliasDAO.findByPlayerAndNickname(player.getKey(), player.getNickname());
		
		if (alias == null) {
			alias = new Alias();
			alias.setPlayer(player.getKey());
			alias.setCount(1L);
			alias.setCreated(new Date());
			alias.setNickname(player.getNickname());
			alias.setServer(player.getServer());
		} else {
			if (update) {
				alias.setCount(alias.getCount() + 1L);
			}
		}
		alias.setUpdated(player.getUpdated());
		
		aliasDAO.save(alias);
		
		AliasIP aliasIP = ipDAO.findByPlayerAndIp(player.getKey(), player.getIp());
		
		if (aliasIP == null) {
			aliasIP = new AliasIP();
			aliasIP.setPlayer(player.getKey());
			aliasIP.setCreated(new Date());
			aliasIP.setIp(player.getIp());
			aliasIP.setCount(1L);
		} else {
			if (update) {
				aliasIP.setCount(aliasIP.getCount() + 1L);
			}
		}
		aliasIP.setUpdated(player.getUpdated());
		
		ipDAO.save(aliasIP);
		
		return alias;
	}

}
