package jipdbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import jipdbs.data.Alias;
import jipdbs.data.Player;
import jipdbs.data.Server;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class JIPDBS extends JIPDBSCore {

	private static final int MIN_NGRAM_QUERY = 2;
	private static final int MAX_NGRAM_QUERY = 8;

	private static final Logger log = Logger.getLogger(JIPDBS.class.getName());

	private final String recaptchaPublicKey;
	private final String recaptchaPrivateKey;

	public JIPDBS(Properties props) {
		recaptchaPublicKey = props.getProperty("recaptcha.public.key", "");
		recaptchaPrivateKey = props.getProperty("recaptcha.private.key", "");
	}

	public String getNewRecaptchaCode() {
		ReCaptcha c = ReCaptchaFactory.newReCaptcha(recaptchaPublicKey,
				recaptchaPrivateKey, false);
		return c.createRecaptchaHtml(null, null);
	}

	public boolean isRecaptchaValid(String remoteAddr, String challenge,
			String uresponse) {
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(recaptchaPrivateKey);
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
				challenge, uresponse);

		return reCaptchaResponse.isValid();
	}

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

	public Server getServer(String encodedKey) throws EntityNotFoundException {
		return getServer(KeyFactory.stringToKey(encodedKey));
	}

	public Server getServer(Key key) throws EntityNotFoundException {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return serverDAO.get(service, key);
	}

	public void saveServer(String key, String name, String admin, String ip) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Server server = getServer(key);
			server.setName(name);
			server.setAdmin(new Email(admin));
			server.setAddress(ip);
			serverDAO.save(service, server);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				result.setLatest(alias.getUpdated());
				result.setPlaying(false);
				// result.setPlaying(player.getUpdated().equals(server.getUpdated()));
				result.setName(alias.getNickname());
				result.setServer(server);
				result.setBanInfo(player.getBanInfo());
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
				if (aliasses.size() == 0 && query.length() >= MIN_NGRAM_QUERY)
					aliasses = aliasDAO.findByNGrams(
							service,
							query.length() <= MAX_NGRAM_QUERY ? query : query
									.substring(0, MAX_NGRAM_QUERY), offset,
							limit, count);
			} else if ("ip".equals(type)) {
				aliasses = aliasDAO.findByIP(service, query, offset, limit,
						count);
			} else if ("s".equals(type)) {
				aliasses = aliasDAO.findByServer(service, query, offset, limit, count);
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
				result.setLatest(alias.getUpdated());
				result.setPlaying(false);
				//result.setPlaying(player.getUpdated().equals(server.getUpdated()));
				result.setName(alias.getNickname());
				result.setServer(server);
				result.setBanInfo(player.getBanInfo());
				results.add(result);
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("Unable to fetch players:" + e.getMessage());
			count[0] = 0;
			return Collections.emptyList();
		}
	}

	public Player getPlayer(String player) throws EntityNotFoundException {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return playerDAO.get(service, KeyFactory.stringToKey(player));
	}

	public Alias getLastAlias(String player) {
		DatastoreService service = DatastoreServiceFactory
				.getDatastoreService();
		return aliasDAO.getLastUsedAlias(service,
				KeyFactory.stringToKey(player));
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
					item.setUpdated(alias.getUpdated());
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
