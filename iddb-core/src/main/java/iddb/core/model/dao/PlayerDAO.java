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

import iddb.core.model.Player;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Collection;
import java.util.List;

public interface PlayerDAO {

	public abstract Player findByServerAndGuid(Long server, String guid);

	public abstract List<Player> findByServer(String query, int offset,
			int limit, int[] count);

	public abstract List<Player> findLatest(int offset, int limit, int[] count);

	public abstract List<Player> findBanned(int offset, int limit, int[] count);

	public abstract Player get(Long player) throws EntityDoesNotExistsException;

	public abstract void save(Player player);

	public abstract void save(Collection<Player> players);

	public abstract void save(Player player, boolean commit);

	public abstract void save(Collection<Player> players, boolean commit);

	public abstract void cleanConnected(Long server);

	public abstract int countConnected(Long key);

	public abstract List<Player> findByClientId(String query, int offset,
			int limit, int[] count);

}