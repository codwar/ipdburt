package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.Penalty;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface PenaltyDAO {

	public abstract void save(Penalty penalty);

	public abstract void delete(Penalty penalty);
	
	public abstract Penalty get(Key key) throws EntityNotFoundException;

	public abstract List<Penalty> findByPlayer(Key player);
	
	public abstract List<Penalty> findByPlayer(Key player, int limit);

	public abstract List<Penalty> findByType(Long type, int offset,
			int limit, int[] count);

	public abstract List<Penalty> findByPlayerAndTypeAndActive(Key player, Long type);
	
	public abstract List<Penalty> findByPlayerAndType(Key player, Long type,
			int offset, int limit, int[] count);

	
	/**
	 * Save a list of Penalty.
	 * This is a batch method. It wont return the object key
	 * @param list
	 */
	public abstract void save(List<Penalty> list);
	
	public abstract void delete(List<Penalty> list);

	public abstract List<Penalty> findByPlayerAndType(Key player, Long type);

}