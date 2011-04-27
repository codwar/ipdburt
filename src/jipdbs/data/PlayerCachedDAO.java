package jipdbs.data;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class PlayerCachedDAO implements PlayerDAO {
	
	private final PlayerDAO impl;
	
	public PlayerCachedDAO(PlayerDAO impl) {
		this.impl = impl;
	}

	@Override
	public void save(Player player) {
		impl.save(player);
	}

	@Override
	public Player findByServerAndGuid(Key server, String guid) {
		return impl.findByServerAndGuid(server, guid);
	}

	@Override
	public List<Player> findLatest(int offset, int limit, int[] count) {
		return impl.findLatest(offset, limit, count);
	}

	@Override
	public List<Player> findBanned(int offset, int limit, int[] count) {
		return impl.findBanned(offset, limit, count);
	}

	@Override
	public Player get(Key player) throws EntityNotFoundException {
		return impl.get(player);
	}

	@Override
	public void truncate() {
		impl.truncate();
	}
}
