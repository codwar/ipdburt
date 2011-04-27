package jipdbs.data;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public interface PlayerDAO {

	public abstract void save(Player player);

	public abstract Player findByServerAndGuid(Key server, String guid);

	public abstract List<Player> findLatest(int offset, int limit, int[] count);

	public abstract List<Player> findBanned(int offset, int limit, int[] count);

	public abstract Player get(Key player) throws EntityNotFoundException;

	public abstract void truncate();

}