package jipdbs.task;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import jipdbs.core.model.Player;

public class PenaltyTask {

	private static final String URL = "";
	
	public static void process(Player player) {
		Queue queue = QueueFactory.getQueue("penalty");
		queue.add(TaskOptions.Builder.withUrl(URL).param("key", KeyFactory.keyToString(player.getKey())));
	}
}
