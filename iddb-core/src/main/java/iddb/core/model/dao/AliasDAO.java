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

import java.util.Collection;
import java.util.List;

public interface AliasDAO {

	public abstract List<Alias> findByNickname(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByNGrams(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByPlayer(Long player, int offset,
			int limit, int[] count);

	public abstract void save(Alias alias);

	public abstract void save(Collection<Alias> aliasses);

	public abstract void save(Alias alias, boolean commit);

	public abstract void save(Collection<Alias> aliasses, boolean commit);

	public abstract Alias findByPlayerAndNickname(Long player, String nickname);

}