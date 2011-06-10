package jipdbs.admin.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jipdbs.admin.Command;
import jipdbs.admin.utils.EntityIterator;
import jipdbs.admin.utils.EntityIterator.Callback;
import jipdbs.core.model.Alias;
import jipdbs.core.model.AliasIP;
import jipdbs.core.model.Player;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.model.dao.impl.AliasDAOImpl;
import jipdbs.core.util.LocalCache;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

public class UpdateAliasIp extends Command {

	static int count = 0;

	@Override
	protected void execute(String[] args) throws Exception {
		final long maxEntities = 10000000000L;

		final AliasDAO aliasDAO = new AliasDAOImpl();

		Query playerQuery = new Query("Player");
		
		count = 0;

		initializeState("updatealiasip");

		Cursor cursor = null;
		int offset = 0;
		boolean force = false;
		try {
			if ("force".equalsIgnoreCase(args[0])) {
				force = true;
			}
		} catch (Exception e) {
		}

		try {
			offset = Integer.parseInt(args[1]);
		} catch (Exception e) {
		}
		
		if (!force) cursor = loadCursor();
		
		if (offset > 0) {
			/* TRY TO GET A CURSOR FROM THE OFFSET */
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = ds.prepare(new Query("Player").setKeysOnly());
			QueryResultList<Entity> q = pq.asQueryResultList(FetchOptions.Builder.withLimit(1).offset(offset));
			cursor = q.getCursor();
		}
		
		if (cursor == null) {
			System.out.println("Starting process");	
		} else {
			System.out.println("Resuming from previous state");
		}
		
		EntityIterator.iterate(playerQuery, maxEntities, cursor, new Callback() {
			@SuppressWarnings("deprecation")
			@Override
			public void withEntity(Entity entity, DatastoreService ds, Cursor cursor, long total)
					throws Exception {

				final Player player = new Player(entity);

				count = count + 1;

				saveCursor(cursor);
				
				if (player.getNickname() != null) return;

				Alias lastAlias = aliasDAO.getLastUsedAlias(player.getKey());

				player.setNickname(lastAlias.getNickname());
				//player.setIp(lastAlias.getIp());

				List<Alias> aliases = aliasDAO.findByPlayer(player.getKey(), 0,
						1000, null);

				List<Key> deleteAlias = new ArrayList<Key>();
				Map<String, Entity> mapAlias = new LinkedHashMap<String, Entity>();
				Map<String, Entity> mapIP = new LinkedHashMap<String, Entity>();

				for (Alias alias : aliases) {
					deleteAlias.add(alias.getKey());
					if (alias.getCreated() == null) alias.setCreated(new Date());
					if (alias.getUpdated() == null) alias.setUpdated(new Date());
					Alias newAlias = null;
					if (mapAlias.containsKey(alias.getNickname())) {
						newAlias = new Alias(mapAlias.get(alias.getNickname()));
						newAlias.setCount(newAlias.getCount() + 1L);
						if (alias.getUpdated().after(newAlias.getUpdated())) {
							newAlias.setUpdated(alias.getUpdated());
						}
						if (alias.getCreated().before(newAlias.getCreated())) {
							newAlias.setCreated(alias.getCreated());
						}
					} else {
						newAlias = new Alias(player.getKey());
						newAlias.setCount(1L);
						newAlias.setNickname(alias.getNickname());
						newAlias.setCreated(alias.getCreated());
						newAlias.setUpdated(alias.getUpdated());
						newAlias.setNgrams(alias.getNgrams());
						newAlias.setServer(player.getServer());
					}
					mapAlias.put(alias.getNickname(), newAlias.toEntity());
					AliasIP newIpAlias = null;
//					if (mapIP.containsKey(alias.getIp())) {
//						newIpAlias = new AliasIP(mapIP.get(alias.getIp()));
//						newIpAlias.setCount(newIpAlias.getCount() + 1L);
//						if (alias.getUpdated().after(newIpAlias.getUpdated())) {
//							newIpAlias.setUpdated(alias.getUpdated());
//						}
//						if (alias.getCreated().before(newIpAlias.getCreated())) {
//							newIpAlias.setCreated(alias.getCreated());
//						}
//					} else {
//						newIpAlias = new AliasIP(player.getKey());
//						newIpAlias.setCount(1L);
//						newIpAlias.setCreated(alias.getCreated());
//						newIpAlias.setIp(alias.getIp());
//						newIpAlias.setUpdated(alias.getUpdated());
//					}
//					mapIP.put(alias.getIp(), newIpAlias.toEntity());
				}

				Transaction tx = ds.beginTransaction();
				try {
					ds.put(player.toEntity());
					ds.put(mapAlias.values());
					ds.put(mapIP.values());
					ds.delete(deleteAlias);

					tx.commit();
				} catch (Exception e) {
					System.err.println("Player: " + player.getGuid());
					System.err.println(e.getMessage());
				} finally {
					if (tx.isActive())
						tx.rollback();
				}

				System.out.println(count + " - %" + (count * 100) / total);

			}
		});

		LocalCache.getInstance().clearAll();
		
		System.out.print("Done");

	}

}
