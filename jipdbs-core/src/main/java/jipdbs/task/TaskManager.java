package jipdbs.task;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import jipdbs.core.model.Player;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.model.dao.AliasIPDAO;
import jipdbs.core.model.dao.PlayerDAO;
import jipdbs.core.model.dao.ServerDAO;
import jipdbs.core.model.dao.cached.AliasCachedDAO;
import jipdbs.core.model.dao.cached.AliasIPCachedDAO;
import jipdbs.core.model.dao.cached.PlayerCachedDAO;
import jipdbs.core.model.dao.cached.ServerCachedDAO;
import jipdbs.core.model.dao.impl.AliasDAOImpl;
import jipdbs.core.model.dao.impl.AliasIPDAOImpl;
import jipdbs.core.model.dao.impl.PlayerDAOImpl;
import jipdbs.core.model.dao.impl.ServerDAOImpl;

public class TaskManager {

	private static final Logger log = Logger.getLogger(TaskManager.class.getName());

	protected final ServerDAO serverDAO = new ServerCachedDAO(new ServerDAOImpl());
	protected final PlayerDAO playerDAO = new PlayerCachedDAO(new PlayerDAOImpl());
	protected final AliasDAO aliasDAO = new AliasCachedDAO(new AliasDAOImpl());
	protected final AliasIPDAO aliasIpDAO = new AliasIPCachedDAO(new AliasIPDAOImpl());
	
	private PenaltyTask penaltyTask = new PenaltyTask();
	
	public void processPenalty(String key, String event) {
		try {
			Player player = playerDAO.get(KeyFactory.stringToKey(key));
			penaltyTask.process(player, event);
		} catch (EntityNotFoundException e) {
			log.severe(e.getMessage());
		}
	}
	
}
