package jipdbs.core.model.dao;

import java.util.Collection;
import java.util.List;

import jipdbs.core.model.Alias;

import com.google.appengine.api.datastore.Key;

public interface AliasDAO {

	@Deprecated
	public abstract Alias findByPlayerAndNicknameAndIp(Key player,
			String nickname, String ip);

	@Deprecated
	public abstract Alias getLastUsedAlias(Key player);

	public abstract List<Alias> findByNickname(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByNGrams(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByPlayer(Key player, int offset, int limit,
			int[] count);

	@Deprecated
	public abstract List<Alias> findByIP(String query, int offset, int limit,
			int[] count);

	public abstract List<Alias> findByServer(String query, int offset,
			int limit, int[] count);

	public abstract void truncate();

	public abstract void save(Alias alias);

	public abstract void save(Collection<Alias> aliasses);

	public abstract void save(Alias alias, boolean commit);

	public abstract void save(Collection<Alias> aliasses, boolean commit);
	
	public abstract Alias findByPlayerAndNickname(Key player, String nickname);

}