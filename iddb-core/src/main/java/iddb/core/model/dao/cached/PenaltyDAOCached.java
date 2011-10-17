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

import iddb.core.model.Penalty;
import iddb.core.model.dao.PenaltyDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public class PenaltyDAOCached extends CachedDAO implements PenaltyDAO {

	private PenaltyDAO impl;
	
	public PenaltyDAOCached(PenaltyDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	private String cacheKey(Long key) {
		return "key-" + key.toString();
	}
	
	@Override
	public void save(Penalty penalty) {
		impl.save(penalty);
		cachePut(cacheKey(penalty.getKey()), penalty);
	}

	@Override
	public Penalty get(Long key) throws EntityDoesNotExistsException {
		Penalty penalty = (Penalty) cacheGet(cacheKey(key));
		if (penalty == null) {
			penalty = impl.get(key);
			if (penalty != null) cachePut(cacheKey(key), penalty);
		}
		return penalty;
	}

	@Override
	public List<Penalty> findByPlayer(Long player) {
		return this.findByPlayer(player, 1000);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Penalty> findByPlayer(Long player, int limit) {
		String cachekey = "ply-" + player.toString() + "L" + Integer.toString(limit);
		List<Penalty> list = (List<Penalty>) cacheGet(cachekey);
		if (list != null) return list;
		list = impl.findByPlayer(player, limit);
		cachePut(cachekey, list);
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Penalty> findByType(Long type, int offset, int limit, int[] count) {
		String key = "type-" + type.toString() + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);;
		List<Penalty> list = (List<Penalty>) getCachedList(key, count);
		if (list != null) return list;
		list = impl.findByType(type, offset, limit, count);
		putCachedList(key, list, count);
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Penalty> findByPlayerAndType(Long player, Integer type, int offset, int limit, int[] count) {
		String key = "ptype-" + player.toString() + "T" + type.toString() + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);;
		List<Penalty> list = (List<Penalty>) getCachedList(key, count);
		if (list != null) return list;
		list = impl.findByPlayerAndType(player, type, offset, limit, count);
		putCachedList(key, list, count);
		return list;
	}

	@Override
	protected void initializeCache() {
		createCache("penalty");
	}

	@Override
	public void save(List<Penalty> list) {
		impl.save(list);
	}

	@Override
	public void delete(Penalty penalty) {
		impl.delete(penalty);
		cacheClear();
		
	}

	@Override
	public void delete(List<Penalty> list) {
		impl.delete(list);
		cacheClear();
	}

	@Override
	public List<Penalty> findByPlayerAndTypeAndActive(Long player, Integer type) {
		return impl.findByPlayerAndTypeAndActive(player, type);
	}

	@Override
	public List<Penalty> findByPlayerAndType(Long player, Integer type) {
		return impl.findByPlayerAndType(player, type);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#disable(java.util.List)
	 */
	@Override
	public void disable(List<Penalty> list) {
		impl.disable(list);
		cacheClear();
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#findLastActivePenalty(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Penalty findLastActivePenalty(Long player, Integer type) {
		String key = "active-" + player.toString() + "T" + type.toString();;
		Penalty penalty = (Penalty) cacheGet(key);
		if (penalty == null) {
			penalty = impl.findLastActivePenalty(player, type);
			if (penalty != null) cachePut(key, penalty);
		}
		return penalty;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#findExpired()
	 */
	@Override
	public List<Penalty> findExpired() {
		return impl.findExpired();
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#deletePlayerPenalty(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public void deletePlayerPenalty(Long player, Integer type) {
		impl.deletePlayerPenalty(player, type);
	}

}
