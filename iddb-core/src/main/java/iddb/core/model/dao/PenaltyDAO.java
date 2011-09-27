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

import iddb.core.model.Penalty;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public interface PenaltyDAO {

	public abstract void save(Penalty penalty);

	public abstract void delete(Penalty penalty);
	
	public abstract Penalty get(Long key) throws EntityDoesNotExistsException;

	public abstract List<Penalty> findByPlayer(Long player);
	
	public abstract List<Penalty> findByPlayer(Long player, int limit);

	public abstract List<Penalty> findByType(Long type, int offset,
			int limit, int[] count);

	public abstract List<Penalty> findByPlayerAndTypeAndActive(Long player, Integer type);
	
	public abstract Penalty findLastActivePenalty(Long player, Integer type);
	
	public abstract List<Penalty> findByPlayerAndType(Long player, Integer type,
			int offset, int limit, int[] count);

	/**
	 * Save a list of Penalty.
	 * This is a batch method. It wont return the object key
	 * @param list
	 */
	public abstract void save(List<Penalty> list);
	
	public abstract void disable(List<Penalty> list);
	
	public abstract void delete(List<Penalty> list);

	public abstract List<Penalty> findByPlayerAndType(Long player, Integer type);

}