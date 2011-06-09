package jipdbs.admin.commands;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import jipdbs.admin.Command;
import jipdbs.core.model.Player;
import jipdbs.info.BanInfo;

public class Test extends Command {

	@Override
	protected void execute(String[] args) throws Exception {
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = ds.prepare(new Query("Player"));
		
		for (Entity entity : pq.asIterable()) {
			Player player = new Player(entity);
			System.out.println(BanInfo.getDetail(player.getBanInfo()));
		}
		
	}

}
