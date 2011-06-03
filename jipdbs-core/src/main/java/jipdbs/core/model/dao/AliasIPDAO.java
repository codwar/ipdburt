package jipdbs.core.model.dao;

import java.util.List;

import jipdbs.core.model.AliasIP;

import com.google.appengine.api.datastore.Key;

public interface AliasIPDAO {

	public abstract void save(AliasIP alias);

	public abstract AliasIP findByPlayerAndIp(Key player, String ip);

	public abstract List<AliasIP> findByPlayer(Key player, int offset,
			int limit, int[] count);

	public abstract List<AliasIP> findByIP(String query, int offset, int limit,
			int[] count);

}