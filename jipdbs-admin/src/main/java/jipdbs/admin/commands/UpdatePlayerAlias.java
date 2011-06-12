package jipdbs.admin.commands;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jipdbs.admin.Command;
import jipdbs.admin.utils.EntityIterator;
import jipdbs.admin.utils.EntityIterator.Callback;
import jipdbs.core.model.Alias;
import jipdbs.core.model.AliasIP;
import jipdbs.core.model.Player;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.model.dao.AliasIPDAO;
import jipdbs.core.model.dao.PlayerDAO;
import jipdbs.core.model.dao.impl.AliasDAOImpl;
import jipdbs.core.model.dao.impl.AliasIPDAOImpl;
import jipdbs.core.model.dao.impl.PlayerDAOImpl;
import jipdbs.core.util.Functions;
import jipdbs.core.util.NGrams;
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
import com.google.appengine.api.datastore.Query.SortDirection;

public class UpdatePlayerAlias extends Command {

	static int count = 0;

	@Override
	protected void execute(OptionSet options) throws Exception {

		initializeState("updateplayer");
		
		int limit = 1000;
		if (options.hasArgument("limit")) {
			limit = (Integer) options.valueOf("limit");
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
		
		final Map<Key, Player> playersMap = new HashMap<Key, Player>();
		final Map<String, Object> aliasCache = new HashMap<String, Object>();

		final PlayerDAO playerDAO = new PlayerDAOImpl();
		final AliasDAO aliasDAO = new AliasDAOImpl();
		final AliasIPDAO aliasIPDAO = new AliasIPDAOImpl();
		
		EntityIterator.iterate("Alias", limit, cursor, new Callback() {
			@Override
			public void withEntity(Entity entity, DatastoreService ds, Cursor cursor, long total)
					throws Exception {

				count = count + 1;

				saveCursor(cursor);

				try {
					Player player;
					if (playersMap.containsKey(entity.getParent())) {
						player = playersMap.get(entity.getParent());
					} else {
						player = playerDAO.get(entity.getParent());
					}
					
					if (player != null) {
						
						if (player.getNickname() == null) {
							DatastoreService service = DatastoreServiceFactory.getDatastoreService();
							Query q = new Query("Alias");
							q.setAncestor(player.getKey());
							q.addSort("updated", SortDirection.DESCENDING);

							PreparedQuery pq = service.prepare(q);
							List<Entity> list = pq.asList(withLimit(1));
							if (list.size() > 0) {
								Entity lastAlias = list.get(0);
								player.setNickname((String) lastAlias.getProperty("nickname"));
								player.setIp(Functions.decimalToIp((Long) lastAlias.getProperty("ip")));
								playerDAO.save(player);
							}
						}

						playersMap.put(entity.getParent(), player);
						
						Alias alias = null;
						String aliasKey = "alias"+KeyFactory.keyToString(player.getKey())+(String) entity.getProperty("nickname");
						if (aliasCache.containsKey(aliasKey)) {
							alias = (Alias) aliasCache.get(aliasKey);
						} else {
							alias = aliasDAO.findByPlayerAndNickname(player.getKey(), (String) entity.getProperty("nickname"));
						}
						if (alias == null) {
							alias = new Alias(player.getKey());
							alias.setNickname((String) entity.getProperty("nickname"));
							alias.setCreated((Date) entity.getProperty("created"));
							alias.setUpdated((Date) entity.getProperty("updated"));
							alias.setCount((Long) entity.getProperty("count"));
							alias.setServer(player.getServer());
							alias.setNgrams(NGrams.ngrams(alias.getNickname()));
						} else {
							alias.setCount(alias.getCount() + (Long) entity.getProperty("count"));
							try {
								if (alias.getUpdated().before((Date) entity.getProperty("updated"))) {
									alias.setUpdated((Date) entity.getProperty("updated"));
								}
							} catch (NullPointerException e) {
								if (alias.getUpdated() == null) alias.setUpdated(new Date());
							}
							
							try {
								if (alias.getCreated().before((Date) entity.getProperty("created"))) {
									alias.setCreated((Date) entity.getProperty("created"));
								}
							} catch (NullPointerException e) {
								if (alias.getCreated() == null) alias.setCreated(new Date());
							}
						}
						
						aliasDAO.save(alias);
						aliasCache.put(aliasKey, alias);
						
						AliasIP aliasIP = null;
						String aliasIPKey = "aliasip"+KeyFactory.keyToString(player.getKey())+ Long.toString((Long) entity.getProperty("ip"));
						if (aliasCache.containsKey(aliasIPKey)) {
							aliasIP = (AliasIP) aliasCache.get(aliasIPKey);
						} else {
							aliasIP = aliasIPDAO.findByPlayerAndIp(player.getKey(), Functions.decimalToIp((Long) entity.getProperty("ip")));
						}					
						if (aliasIP == null) {
							aliasIP = new AliasIP(player.getKey());
							aliasIP.setIp(Functions.decimalToIp((Long) entity.getProperty("ip")));
							aliasIP.setCreated((Date) entity.getProperty("created"));
							aliasIP.setUpdated((Date) entity.getProperty("updated"));
							aliasIP.setCount(1l);
						} else {
							aliasIP.setCount(aliasIP.getCount() + 1l);
							try {
								if (aliasIP.getUpdated().before((Date) entity.getProperty("updated"))) {
									aliasIP.setUpdated((Date) entity.getProperty("updated"));
								}
							} catch (NullPointerException e) {
								if (aliasIP.getUpdated() == null) aliasIP.setUpdated(new Date());
							}
							
							try {
								if (aliasIP.getCreated().before((Date) entity.getProperty("created"))) {
									aliasIP.setCreated((Date) entity.getProperty("created"));
								}
							} catch (NullPointerException e) {
								if (aliasIP.getCreated() == null) aliasIP.setCreated(new Date());
							}
						}
						
						aliasIPDAO.save(aliasIP);
						aliasCache.put(aliasIPKey, aliasIP);
						
					}
					
					ds.delete(entity.getKey());
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println(count);

			}
		});

		System.out.println("Done");

	}

	@Override
	public OptionParser getCommandOptions() {
		OptionParser parser = new OptionParser() {
            {
                acceptsAll( Arrays.asList("l", "limit") ).withOptionalArg().ofType(Integer.class)
                    .describedAs( "limit" ).defaultsTo( 1000 );
                acceptsAll( Arrays.asList("f", "force"), "do not resume");
            }
        };
		return parser;
	}

}
