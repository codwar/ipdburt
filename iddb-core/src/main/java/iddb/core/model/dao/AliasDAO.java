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

import iddb.core.model.Alias;
import iddb.core.model.Player;
import iddb.core.model.dao.cached.AliasDAOCached;

import java.util.Collection;
import java.util.List;

public interface AliasDAO {

	@SuppressWarnings("rawtypes")
	public static final Class cached = AliasDAOCached.class;
	
	/**
	 * List aliases by nickname
	 * @param query
	 * @param offset
	 * @param limit
	 * @param count
	 * @return
	 */
	public abstract List<Alias> findByNickname(String query, int offset,
			int limit, int[] count);

	/**
	 * List aliases with similar name
	 * @param query
	 * @param offset
	 * @param limit
	 * @param count
	 * @return
	 */
	public abstract List<Player> findBySimilar(String query, Long server, int offset,
			int limit, int[] count);

	/**
	 * List aliases for player id
	 * @param player
	 * @param offset
	 * @param limit
	 * @param count
	 * @return
	 */
	public abstract List<Alias> findByPlayer(Long player, int offset,
			int limit, int[] count);

	/**
	 * Save alias. Commit by default
	 * @param alias
	 */
	public abstract void save(Alias alias);

	/**
	 * Save alias
	 * @param aliasses
	 */
	public abstract void save(Collection<Alias> aliasses);

	/**
	 * List by nickname for player id
	 * @param player
	 * @param nickname
	 * @return
	 */
	public abstract Alias findByPlayerAndNickname(Long player, String nickname);

	/**
	 * @param query
	 * @param serverkey
	 * @param offset
	 * @param limit
	 * @param count
	 * @return
	 */
	public abstract List<Player> findBySimilar(String[] query, Long serverkey,
			int offset, int limit, int[] count);

}