package jipdbs.admin.commands;

import jipdbs.admin.Command;
import jipdbs.admin.utils.EntityIterator;
import jipdbs.admin.utils.EntityIterator.Callback;
import jipdbs.core.model.Player;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

public class BanInfoDateUpdate extends Command {

	@Override
	protected void execute(String[] args) throws Exception {
		long maxEntities = 10000000000L;

		EntityIterator.iterate("Player", maxEntities, new Callback() {

			@Override
			public void withEntity(Entity entity, DatastoreService ds)
					throws Exception {

				Player player = new Player(entity);

				if (player.getBanInfo() != null) {
					entity.setProperty("baninfoupdated", player.getUpdated());
				} else
					entity.setProperty("baninfoupdated", null);

				ds.put(entity);
			}
		});

	}
}
