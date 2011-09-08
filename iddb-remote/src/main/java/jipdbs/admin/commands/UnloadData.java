package jipdbs.admin.commands;

import iddb.core.model.Alias;
import iddb.core.model.AliasIP;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.model.dao.AliasDAO;
import iddb.core.model.dao.ServerDAO;
import iddb.core.model.dao.impl.AliasDAOImpl;
import iddb.core.model.dao.impl.ServerDAOImpl;
import iddb.core.util.Functions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jipdbs.admin.Command;
import jipdbs.admin.utils.EntityIterator;
import jipdbs.admin.utils.EntityIterator.Callback;
import jipdbs.admin.utils.EscapeChars;
import jipdbs.admin.utils.Transformer;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class UnloadData extends Command {

	static int count = 0;

	@Override
	protected void execute(OptionSet options) throws Exception {
		final int maxEntities = Integer.MAX_VALUE;

		if (!options.hasArgument("output")) return;
		
		final AliasDAO aliasDAO = new AliasDAOImpl();

		String file = (String) options.valueOf("output");
		
		Writer wrt = new FileWriter(file); 
		
		System.out.println("Writing to " + file);

		int limit = maxEntities;
		if (options.hasArgument("limit")) {
			limit = (Integer) options.valueOf("limit");
		}
		
		initializeState("unloaddata");
		
		boolean force = false;
		if (options.has("force")) {
			force = true;
		}

		Cursor cursor = null;
		if (!force) cursor = loadCursor();
		
		count = 0;
		
		if (cursor == null) {
			System.out.println("Starting process");	
		} else {
			System.out.println("Resuming from previous state");
		}
		
		final BufferedWriter out = new BufferedWriter(wrt);
		
		EntityIterator.iterate("Player", limit, 100, cursor, new Callback() {
			@SuppressWarnings("deprecation")
			@Override
			public void withEntity(Entity entity, DatastoreService ds, Cursor cursor, long total)
					throws Exception {

				final Player player = new Player(entity);

				count = count + 1;
				
				Alias lastAlias = null;
				
				if (player.getNickname() != null) {
					lastAlias = new Alias(player.getKey());
					lastAlias.setNickname(player.getNickname());
					//lastAlias.setIp(player.getIp());
				} else {
					lastAlias = aliasDAO.getLastUsedAlias(player.getKey());	
				}
				if (lastAlias == null) return;

				/* PROCESS SERVER */
				ServerDAO serverDAO = new ServerDAOImpl();
				Server server = serverDAO.get(player.getServer());
				if (server == null) return;
				StringBuilder serverb = new StringBuilder();
				serverb.append("\"uid\":").append(EscapeChars.toString(server.getUid())).append(",");
				serverb.append("\"name\":").append(EscapeChars.toString(server.getName())).append(",");
				serverb.append("\"admin\":").append(EscapeChars.toString(server.getAdminEmail().getEmail())).append(",");
				serverb.append("\"created\":").append(EscapeChars.toString(Transformer.date_to_string(server.getCreated()))).append(",");
				serverb.append("\"updated\":").append(EscapeChars.toString(Transformer.date_to_string(server.getUpdated()))).append(",");
				serverb.append("\"address\":").append(EscapeChars.toString(server.getAddress())).append(",");
				serverb.append("\"pluginversion\":").append(EscapeChars.toString(server.getPluginVersion())).append(",");
				serverb.append("\"maxlevel\":").append(EscapeChars.toString(server.getMaxLevel())).append(",");
				
				/* PROCESS PLAYER ALIASES */
				final List<String> aliases = new ArrayList<String>();
				Query aliasQuery = new Query("PlayerAlias");
				aliasQuery.setAncestor(player.getKey());
				EntityIterator.iterate(aliasQuery, Integer.MAX_VALUE, null, new Callback() {
					@Override
					public void withEntity(Entity entity, DatastoreService ds, Cursor cursor, long total) throws Exception {
						Alias alias = new Alias(entity);
						StringBuilder a = new StringBuilder();
						a.append("\"nickname\":").append(EscapeChars.toString(EscapeChars.forJSON(alias.getNickname()))).append(",");
						a.append("\"created\":").append(EscapeChars.toString(Transformer.date_to_string(alias.getCreated()))).append(",");
						a.append("\"updated\":").append(EscapeChars.toString(Transformer.date_to_string(alias.getUpdated()))).append(",");
						a.append("\"count\":").append(EscapeChars.toString(alias.getCount())).append(",");
						StringBuilder ngrams = new StringBuilder();
						if (alias.getNgrams() != null) {
							for (Iterator<String> it = alias.getNgrams().iterator(); it.hasNext() ; ) {
								ngrams.append(EscapeChars.toString(EscapeChars.forJSON(it.next())));
								if (it.hasNext()) ngrams.append(",");
							}
						}
						a.append("\"ngrams\": [").append(ngrams.toString()).append("]");
						aliases.add("{" + a.toString() + "}");
					}
				});
				
				/* PROCESS PLAYER IPS */
				final List<String> ips = new ArrayList<String>();
				Query ipQuery = new Query("AliasIP");
				ipQuery.setAncestor(player.getKey());
				EntityIterator.iterate(ipQuery, Integer.MAX_VALUE, null, new Callback() {
					@Override
					public void withEntity(Entity entity, DatastoreService ds, Cursor cursor, long total) throws Exception {
						AliasIP alias = new AliasIP(entity);
						StringBuilder a = new StringBuilder();
						a.append("\"ip\":").append(EscapeChars.toString(EscapeChars.forJSON(alias.getIp()))).append(",");
						a.append("\"created\":").append(EscapeChars.toString(Transformer.date_to_string(alias.getCreated()))).append(",");
						a.append("\"updated\":").append(EscapeChars.toString(Transformer.date_to_string(alias.getUpdated()))).append(",");
						a.append("\"count\":").append(EscapeChars.toString(alias.getCount())).append(",");
						ips.add("{" + a.toString() + "}");
					}
				});

				StringBuilder p = new StringBuilder();
				p.append("\"nickname\":").append(EscapeChars.toString(EscapeChars.forJSON(lastAlias.getNickname()))).append(",");
				p.append("\"ip\":").append(EscapeChars.toString(player.getIp())).append(",");
				p.append("\"guid\":").append(EscapeChars.toString(player.getGuid())).append(",");
				p.append("\"baninfo\":").append(EscapeChars.toString(player.getBanInfo())).append(",");
				p.append("\"baninfoupdated\":").append(EscapeChars.toString(Transformer.date_to_string(player.getBanInfoUpdated()))).append(",");
				p.append("\"created\":").append(EscapeChars.toString(Transformer.date_to_string(player.getCreated()))).append(",");
				p.append("\"updated\":").append(EscapeChars.toString(Transformer.date_to_string(player.getUpdated()))).append(",");
				p.append("\"clientid\":").append(EscapeChars.toString(player.getClientId())).append(",");
				p.append("\"level\":").append(EscapeChars.toString(player.getLevel())).append(",");
				p.append("\"note\":").append(EscapeChars.toString(EscapeChars.forJSON(player.getNote()))).append(",");
				p.append("\"server\": {").append(serverb.toString()).append("}").append(",");
				p.append("\"aliasses\":[").append(Functions.join(aliases, ",")).append("]").append(",");
				p.append("\"ipaliasses\":[").append(Functions.join(ips, ",")).append("]");
				
				out.write("{" + p.toString() + "}\n");
				out.flush();
				
				System.out.println(count + " - %" + (count * 100) / total);

			}
		});
		
		out.close();
		wrt.close();
		
		System.out.print("Done");

	}

	@Override
	protected OptionParser getCommandOptions() {
		OptionParser parser = new OptionParser() {
            {
                acceptsAll( Arrays.asList("o", "output"), "output file" ).withRequiredArg().ofType(File.class);
                acceptsAll( Arrays.asList("l", "limit"), "limit" ).withOptionalArg().ofType(Integer.class);
                acceptsAll( Arrays.asList("f", "force"), "do not resume");                
            }
        };
		return parser;
	}
	
}
