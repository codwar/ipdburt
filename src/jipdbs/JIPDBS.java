package jipdbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jipdbs.data.Alias;
import jipdbs.data.Player;
import jipdbs.data.Server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class JIPDBS extends JIPDBSCore {

	private static final Logger log = Logger.getLogger(JIPDBS.class.getName());

	// web fron-end methods ////////////////////////////////////////////////////

	public void addServer(String name, String admin, String uid) {

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
		serverDAO.save(service, server);
	}

	public List<Server> getServers() {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return serverDAO.findAll(service);
	}

	public List<SearchResult> rootQuery() {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		List<Player> players = playerDAO.findLatest(service);

		List<SearchResult> results = new ArrayList<SearchResult>();

		try {
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
				result.setLatest(server.getUpdated()
						.equals(player.getUpdated()) ? "Connected" : player
						.getUpdated().toString());
				result.setName(alias.getNickname());
				result.setServer(server.getName());

				results.add(result);
			}
		} catch (EntityNotFoundException e) {
			// Do nothing...?
			e.printStackTrace();
		}

		return results;
	}

	public List<SearchResult> search(String query, String type) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		List<SearchResult> results = new ArrayList<SearchResult>();

		try {
			List<Alias> aliasses = new ArrayList<Alias>();
			
			if ("alias".equals(type)) {
				aliasses = aliasDAO.findByNickname(service, query);
				// No exact match, try ngrams.
				if (aliasses.size() == 0)
					aliasses = aliasDAO.findByNGrams(service, query);
			} else if ("ip".equals(type)) {
				aliasses = aliasDAO.findByIP(service, query);
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
				result.setLatest(server.getUpdated()
						.equals(player.getUpdated()) ? "Connected" : player
						.getUpdated().toString());
				result.setName(alias.getNickname());
				result.setServer(server.getName());

				results.add(result);
			}
		} catch (EntityNotFoundException e) {
			// TODO Do nothing...?
			e.printStackTrace();
		}

		return results;
	}

	public List<AliasResult> alias(String encodedKey) {

		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();

		List<AliasResult> result = new ArrayList<AliasResult>();

		try {

			Player player = playerDAO.get(service,
					KeyFactory.stringToKey(encodedKey));

			if (player != null) {
				// Server fetched to print the date "Connected".
				// If null, no comparison of dates.
				Server server = serverDAO.get(service, player.getServer());

				List<Alias> aliasses = aliasDAO.findByPlayer(service,
						player.getKey());

				for (Alias alias : aliasses) {

					AliasResult item = new AliasResult();
					item.setCount(alias.getCount());
					item.setIp(alias.getMaskedIp());
					item.setNickname(alias.getNickname());
					item.setUpdated(server != null
							&& server.getUpdated().equals(alias.getUpdated()) ? "Connected"
							: alias.getUpdated().toString());

					result.add(item);
				}
			}

		} catch (EntityNotFoundException e) {
			// TODO Do nothing...?
			e.printStackTrace();
		}
		return result;
	}
}
