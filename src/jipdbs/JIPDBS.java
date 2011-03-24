package jipdbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.data.Alias;
import jipdbs.data.Player;
import jipdbs.data.Server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;

public class JIPDBS extends JIPDBSCore {

	private static final int MAX_NGRAM_QUERY = 8;

	private static final Logger log = Logger.getLogger(JIPDBS.class.getName());

	public void addServer(String name, String admin, String uid, String ip) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		Server server = new Server();
		server.setAdmin(new Email(admin));
		server.setCreated(new Date());
		// server.setUpdated(new Date()); // skip this. it should be updated
		// when the server actually start sending data.
		server.setUid(uid);
		server.setName(name);
		server.setOnlinePlayers(0);
		server.setAddress(ip);
		serverDAO.save(service, server);
	}

	public List<Server> getServers(int offset, int limit, int[] count) {
		try {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();
			return serverDAO.findAll(service, offset, limit, count);
		} catch (Exception e) {
			log.severe("Unable to fetch servers:" + e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> rootQuery(int offset, int limit, int[] count) {

		try {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();

			List<Player> players = playerDAO.findLatest(service, offset, limit,
					count);

			List<SearchResult> results = new ArrayList<SearchResult>();

			for (Player player : players) {

				Alias alias = aliasDAO.getLastUsedAlias(service,
						player.getKey());
				Server server = serverDAO.get(service, player.getServer());

				// Whoops! inconsistent data.
				if (alias == null || server == null)
					continue;

				SearchResult result = new SearchResult();
				result.setKey(KeyFactory.keyToString(player.getKey()));
				result.setIp(alias.getMaskedIp());
				result.setLatest(player.getUpdated()
						.equals(server.getUpdated()) ? "Connected" : player
						.getUpdated().toString());
				result.setName(alias.getNickname());
				result.setServer(server.getName());

				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.severe("Unable to fetch root query players:" + e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<SearchResult> search(String query, String type, int offset,
			int limit, int[] count) {

		try {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();

			List<SearchResult> results = new ArrayList<SearchResult>();

			List<Alias> aliasses = new ArrayList<Alias>();

			if ("alias".equals(type)) {
				aliasses = aliasDAO.findByNickname(service, query, offset,
						limit, count);
				// No exact match, try ngrams.
				if (aliasses.size() == 0)
					aliasses = aliasDAO.findByNGrams(
							service,
							query.length() <= MAX_NGRAM_QUERY ? query : query
									.substring(0, MAX_NGRAM_QUERY), offset,
							limit, count);
			} else if ("ip".equals(type)) {
				aliasses = aliasDAO.findByIP(service, query, offset, limit,
						count);
			}

			for (Alias alias : aliasses) {

				Player player = playerDAO.get(service, alias.getPlayer());
				Server server = serverDAO.get(service, player.getServer());

				// Whoops! inconsistent data.
				if (alias == null || server == null)
					continue;

				SearchResult result = new SearchResult();
				result.setKey(KeyFactory.keyToString(player.getKey()));
				result.setIp(alias.getMaskedIp());
				result.setLatest(player.getUpdated()
						.equals(server.getUpdated()) ? "Connected" : player
						.getUpdated().toString());
				result.setName(alias.getNickname());
				result.setServer(server.getName());

				results.add(result);
			}
			return results;
		} catch (Exception e) {
			log.severe("Unable to fetch players:" + e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public List<AliasResult> alias(String encodedKey, int offset, int limit,
			int[] count) {

		try {
			DatastoreService service = DatastoreServiceFactory
					.getDatastoreService();

			List<AliasResult> result = new ArrayList<AliasResult>();

			Player player = playerDAO.get(service,
					KeyFactory.stringToKey(encodedKey));

			if (player != null) {
				List<Alias> aliasses = aliasDAO.findByPlayer(service,
						player.getKey(), offset, limit, count);

				for (Alias alias : aliasses) {

					AliasResult item = new AliasResult();
					item.setCount(alias.getCount());
					item.setIp(alias.getMaskedIp());
					item.setNickname(alias.getNickname());
					// in aliases we want to know when it was last used
					item.setUpdated(alias.getUpdated().toString());
					result.add(item);
				}
			}

			return result;
		} catch (Exception e) {
			log.severe("Unable to fetch aliasses:" + e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}
}
