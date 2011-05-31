package jipdbs.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public abstract class Command {

	private RemoteApiInstaller installer;
	
	public void run(String[] args) throws Exception {
		try {
			initializeRemoteApi();
			execute(args);
		} finally {
			try {
				this.installer.uninstall();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Perform login
	 * @throws Exception
	 */
	protected void initializeRemoteApi() throws Exception {
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("admin.properties"));
		} catch (Exception e) {
			throw new RuntimeException("File admin.properties not found");
		}		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("username: ");
		String username = console.readLine();
		System.out.print("password: ");
		String password = console.readLine();
		RemoteApiOptions options = new RemoteApiOptions().server(props.getProperty("url"), Integer.parseInt(props.getProperty("port"))).credentials(username, password).remoteApiPath("/remote_api");
		this.installer = new RemoteApiInstaller();
		this.installer.install(options);		
	}
	
	/**
	 * Execute command
	 * @param args
	 * @throws Exception
	 */
	protected abstract void execute(String[] args) throws Exception;
	
}
