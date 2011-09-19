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

import iddb.core.cache.CacheFactory;
import iddb.core.model.Penalty;
import iddb.core.model.dao.PenaltyDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public class PenaltyCachedDAO extends CachedDAO implements PenaltyDAO {

	private PenaltyDAO impl;
	
	public PenaltyCachedDAO(PenaltyDAO impl) {
		this.impl = impl;
		this.initializeCache();
	}

	private String cacheKey(Long key) {
		return "key-" + key.toString();
	}
	
	@Override
	public void save(Penalty penalty) {
		impl.save(penalty);
		cache.put(cacheKey(penalty.getKey()), penalty);
	}

	@Override
	public Penalty get(Long key) throws EntityDoesNotExistsException {
		Penalty penalty = (Penalty) cache.get(cacheKey(key));
		if (penalty == null) {
			penalty = impl.get(key);
			if (penalty != null) cache.put(cacheKey(key), penalty);
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
		List<Penalty> list = (List<Penalty>) cache.get(cachekey);
		if (list != null) return list;
		list = impl.findByPlayer(player, limit);
		cache.put(cachekey, list);
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
	public List<Penalty> findByPlayerAndType(Long player, Long type, int offset, int limit, int[] count) {
		String key = "ptype-" + player.toString() + "T" + type.toString() + "O" + Integer.toString(offset) + "L" + Integer.toString(limit);;
		List<Penalty> list = (List<Penalty>) getCachedList(key, count);
		if (list != null) return list;
		list = impl.findByPlayerAndType(player, type, offset, limit, count);
		putCachedList(key, list, count);
		return list;
	}

	@Override
	protected void initializeCache() {
		this.cache = CacheFactory.getInstance().getCache("penalty");
	}

	@Override
	public void save(List<Penalty> list) {
		impl.save(list);
	}

	@Override
	public void delete(Penalty penalty) {
		impl.delete(penalty);
		this.cache.clear();
		
	}

	@Override
	public void delete(List<Penalty> list) {
		impl.delete(list);
		this.cache.clear();
	}

	@Override
	public List<Penalty> findByPlayerAndTypeAndActive(Long player, Long type) {
		return impl.findByPlayerAndTypeAndActive(player, type);
	}

	@Override
	public List<Penalty> findByPlayerAndType(Long player, Long type) {
		return impl.findByPlayerAndType(player, type);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#disable(java.util.List)
	 */
	@Override
	public void disable(List<Penalty> list) {
		impl.disable(list);
		this.cache.clear();
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.PenaltyDAO#findLastActivePenalty(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Penalty findLastActivePenalty(Long player, Long type) {
		String key = "active-" + player.toString() + "T" + type.toString();;
		Penalty penalty = (Penalty) this.cache.get(key);
		if (penalty == null) {
			penalty = impl.findLastActivePenalty(player, type);
			if (penalty != null) this.cache.put(key, penalty);
		}
		return penalty;
	}

}
