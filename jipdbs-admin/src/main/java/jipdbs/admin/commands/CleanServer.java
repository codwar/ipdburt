package jipdbs.admin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jipdbs.admin.Command;
import jipdbs.admin.utils.EntityIterator;
import jipdbs.admin.utils.EntityIterator.Callback;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class CleanServer extends Command {

	static int count = 0;

	@Override
	protected void execute(OptionSet options) throws Exception {

		String keyStr = null;
		if (options.hasArgument("key")) {
			keyStr = (String) options.valueOf("key");
		} else return;
		
		Key key = null;
		try {
			key = KeyFactory.stringToKey(keyStr);
		} catch (Exception e) {
			throw e;
		}
		
		initializeState("cleanserver"+keyStr);
		
		long limit = 10000000000L;
		if (options.hasArgument("limit")) {
			limit = ((Integer) options.valueOf("limit")).longValue();
		}
		
		boolean force = false;
		if (options.has("force")) {
			force = true;
		}

		Cursor cursor = null;
		if (!force) cursor = loadCursor();
		
		if (cursor == null) {
			System.out.println("Starting process");	
		} else {
			System.out.println("Resuming from previous state");
		}
		
		count = 0;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		// check if the server exists
		Entity server = ds.get(key);
		
		System.out.println("Processing server: " + server.getProperty("name"));
		
		Query players = new Query("Player");
		players.setAncestor(key);
		
		EntityIterator.iterate(players, limit, cursor, new Callback() {
			@Override
			public void withEntity(Entity entity, DatastoreService ds, Cursor cursor,
					long totalElements) throws Exception {

				count = count + 1;

				saveCursor(cursor);
				
				Query q = new Query("PlayerAlias");
				q.setAncestor(entity.getKey());
				PreparedQuery pq = ds.prepare(q);
				
				List<Key> keys = new ArrayList<Key>();
				keys.add(entity.getKey());
				
				for (Entity alias : pq.asIterable()) {
					keys.add(alias.getKey());
				}

				q = new Query("AliasIP");
				q.setAncestor(entity.getKey());
				pq = ds.prepare(q);
				
				for (Entity alias : pq.asIterable()) {
					keys.add(alias.getKey());
				}
				
				ds.delete(keys);
				
				System.out.println("Processed " + count);
				
			}
		});

		System.out.println("Done");

	}

	@Override
	public OptionParser getCommandOptions() {
		OptionParser parser = new OptionParser() {
            {
            	acceptsAll( Arrays.asList("k", "key"), "server key").withRequiredArg().ofType(String.class);
            	acceptsAll( Arrays.asList("l", "limit"), "limit" ).withOptionalArg().ofType(Integer.class);
                acceptsAll( Arrays.asList("f", "force"), "do not resume");
            }
        };
		return parser;
	}

}
