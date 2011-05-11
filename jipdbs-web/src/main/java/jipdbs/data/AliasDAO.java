package jipdbs.data;

import java.util.Collection;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public interface AliasDAO {

	public abstract Alias findByPlayerAndNicknameAndIp(Key player,
			String nickname, String ip);

	public abstract Alias getLastUsedAlias(Key player);

	public abstract List<Alias> findByNickname(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByNGrams(String query, int offset,
			int limit, int[] count);

	public abstract List<Alias> findByPlayer(Key player, int offset, int limit,
			int[] count);

	public abstract List<Alias> findByIP(String query, int offset, int limit,
			int[] count);

	public abstract List<Alias> findByServer(String query, int offset,
			int limit, int[] count);

	public abstract void truncate();

	public abstract void save(Alias alias);

	public abstract void save(Collection<Alias> aliasses);

	public abstract void save(Alias alias, boolean commit);

	public abstract void save(Collection<Alias> aliasses, boolean commit);

}