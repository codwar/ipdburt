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

import iddb.core.model.PenaltyHistory;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public interface PenaltyHistoryDAO {

	public abstract void save(PenaltyHistory history);
	
	public abstract PenaltyHistory get(Long id) throws EntityDoesNotExistsException;
	
	public abstract List<PenaltyHistory> listByPenaltyId(Long id);
	
	public abstract PenaltyHistory getLastByPenalty(Long id) throws EntityDoesNotExistsException;
	
	public abstract List<PenaltyHistory> listByPlayer(Long id, int offset, int limit, int[] count);
	
	public abstract void updateStatus(List<Long> ids, Integer status);
	
}
