package jipdbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jipdbs.EntityIterator.Callback;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public class BanInfoUpdatedUpdate {

	public static void main(String[] args) throws IOException {

		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("username: ");
		String username = console.readLine();
		System.out.print("password: ");
		String password = console.readLine();

		RemoteApiOptions options = new RemoteApiOptions()
				.server("www.ipdburt.com.ar", 443)
				.credentials(username, password).remoteApiPath("/remote_api");
		RemoteApiInstaller installer = new RemoteApiInstaller();
		installer.install(options);
		try {

			long maxEntities = 10000000000L;

			EntityIterator.iterate("Player", maxEntities, new Callback() {

				@Override
				public void withEntity(Entity entity, DatastoreService ds)
						throws Exception {

					Player player = new Player(entity);

					if (player.getBanInfo() != null) {
						entity.setProperty("baninfoupdated",
								player.getUpdated());
					} else
						entity.setProperty("baninfoupdated", null);

					ds.put(entity);
				}
			});

		} finally {
			installer.uninstall();
		}
	}
}
