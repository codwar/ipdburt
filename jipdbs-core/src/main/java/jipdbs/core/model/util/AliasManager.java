package jipdbs.core.model.util;

import java.util.Date;

import jipdbs.core.model.Alias;
import jipdbs.core.model.AliasIP;
import jipdbs.core.model.Player;
import jipdbs.core.model.dao.AliasDAO;
import jipdbs.core.model.dao.AliasIPDAO;
import jipdbs.core.model.dao.cached.AliasCachedDAO;
import jipdbs.core.model.dao.cached.AliasIPCachedDAO;
import jipdbs.core.model.dao.impl.AliasDAOImpl;
import jipdbs.core.model.dao.impl.AliasIPDAOImpl;
import jipdbs.core.util.NGrams;

public class AliasManager {

	private static AliasDAO aliasDAO = new AliasCachedDAO(new AliasDAOImpl());
	private static AliasIPDAO ipDAO = new AliasIPCachedDAO(new AliasIPDAOImpl());
	
	private AliasManager() {};
	
	public static Alias createAlias(Player player, boolean update) {
		Alias alias = aliasDAO.findByPlayerAndNickname(player.getKey(), player.getNickname());
		
		if (alias == null) {
			alias = new Alias(player.getKey());
			alias.setCount(1L);
			alias.setCreated(new Date());
			alias.setNickname(player.getNickname());
			alias.setServer(player.getServer());
			alias.setNgrams(NGrams.ngrams(player.getNickname()));
		} else {
			if (update) {
				alias.setCount(alias.getCount() + 1L);
			}
		}
		alias.setUpdated(player.getUpdated());
		
		aliasDAO.save(alias);
		
		AliasIP aliasIP = ipDAO.findByPlayerAndIp(player.getKey(), player.getIp());
		
		if (aliasIP == null) {
			aliasIP = new AliasIP(player.getKey());
			aliasIP.setCreated(new Date());
			aliasIP.setIp(player.getIp());
			aliasIP.setCount(1L);
		} else {
			if (update) {
				aliasIP.setCount(aliasIP.getCount() + 1L);
			}
		}
		aliasIP.setUpdated(player.getUpdated());
		
		ipDAO.save(aliasIP);
		
		return alias;
	}

}
