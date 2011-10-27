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
package iddb.core.model.dao.cached;

import iddb.core.model.PenaltyHistory;
import iddb.core.model.dao.PenaltyHistoryDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;


public class PenaltyHistoryDAOCached extends CachedDAO implements PenaltyHistoryDAO {

	private PenaltyHistoryDAO impl;
	
	public PenaltyHistoryDAOCached(PenaltyHistoryDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}
	
	/* (non-Javadoc)
	 * @see iddb.core.model.dao.cached.CachedDAO#initializeCache()
	 */
	@Override
	protected void initializeCache() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#save(iddb.core.model.PenaltyHistory)
	 */
	@Override
	public void save(PenaltyHistory history) {
		impl.save(history);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#get(java.lang.Long)
	 */
	@Override
	public PenaltyHistory get(Long id) throws EntityDoesNotExistsException {
		return impl.get(id);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#listByPenaltyId(java.lang.Long)
	 */
	@Override
	public List<PenaltyHistory> listByPenaltyId(Long id) {
		return impl.listByPenaltyId(id);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#getLastByPenalty(java.lang.Long)
	 */
	@Override
	public PenaltyHistory getLastByPenalty(Long id)
			throws EntityDoesNotExistsException {
		return impl.getLastByPenalty(id);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyHistoryDAO#listByPlayer(java.lang.Long, int, int, int[])
	 */
	@Override
	public List<PenaltyHistory> listByPlayer(Long id, int offset, int limit,
			int[] count) {
		return impl.listByPlayer(id, offset, limit, count);
	}

}
