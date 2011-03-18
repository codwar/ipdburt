package jipdbs.data;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class AliasDAO {

	private Entity map(Alias alias) {

		Entity entity = alias.getKey() == null ? new Entity("Alias")
				: new Entity(alias.getKey());

		entity.setProperty("created", alias.getCreated());
		entity.setProperty("updated", alias.getUpdated());
		entity.setProperty("count", alias.getCount());
		entity.setProperty("ip", alias.getIp());
		entity.setProperty("nickname", alias.getNickname());
		entity.setProperty("player", alias.getPlayer());

		return entity;
	}

	private Alias map(Entity entity) {

		Alias alias = new Alias();

		alias.setKey(entity.getKey());
		alias.setCreated((Date) entity.getProperty("created"));
		alias.setUpdated((Date) entity.getProperty("updated"));
		alias.setCount(((Long) entity.getProperty("count")).intValue());
		alias.setIp((String) entity.getProperty("ip"));
		alias.setPlayer((Key) entity.getProperty("player"));
		alias.setNickname((String) entity.getProperty("nickname"));

		return alias;
	}

	public void save(DatastoreService service, Alias alias) {
		Entity entity = map(alias);
		service.put(entity);
		alias.setKey(entity.getKey());
	}

	public Alias findByPlayerAndNicknameAndIp(DatastoreService service,
			Key player, String nickname, String ip) {

		Query q = new Query("Alias");
		q.addFilter("player", FilterOperator.EQUAL, player);
		q.addFilter("nickname", FilterOperator.EQUAL, nickname);
		q.addFilter("ip", FilterOperator.EQUAL, ip);
		PreparedQuery pq = service.prepare(q);
		Entity entity = pq.asSingleEntity();

		if (entity != null)
			return map(entity);

		return null;
	}
}
