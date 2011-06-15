package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.Penalty;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface PenaltyDAO {

	public abstract void save(Penalty penalty);

	public abstract Penalty get(Key key) throws EntityNotFoundException;

	public abstract List<Penalty> findByPlayer(Key player, int limit);

	public abstract List<Penalty> findByType(Integer type, int offset,
			int limit, int[] count);

	public abstract List<Penalty> findByPlayerAndType(Key player, Integer type,
			int offset, int limit, int[] count);

}