package iddb.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class App {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Command command = null;
		try {
			Properties prop = new Properties();
			prop.load(App.class.getClassLoader().getResourceAsStream(
					"commands.properties"));
			if (args.length > 0) {
				for (Object cmdo : prop.keySet()) {
					String cmd = (String) cmdo;
					if (args[0].toLowerCase().equals(cmd.toLowerCase())) {
						Class c = App.class.getClassLoader().loadClass(
								"iddb.cli.command." + cmd);
						command = (Command) c.newInstance();
						break;
					}
					if (command == null)
						System.out.println("Command: " + args[0]
								+ " not found.");
				}
			}
			if (command == null) {
				System.out.println("Available commands are:");
				System.out.println("");
				for (Object cmdo : prop.keySet()) {
					System.out.println(" *  " + cmdo.toString().toLowerCase() + " : " + prop.getProperty(cmdo.toString()));
				}
				System.out.println("");
				System.out
						.println("Use <command> -? : to get the command help");
				System.out.println("");
			} else {
				command.run(Arrays.copyOfRange(args, 1, args.length));
			}
			// Class[] classes = Introspect.getClasses("iddb.cli.command");
			// if (args.length > 0) {
			// for (Class c : classes) {
			// if
			// (args[0].toLowerCase().equals(c.getSimpleName().toLowerCase())) {
			// command = (Command) c.newInstance();
			// break;
			// }
			// }
			// if (command == null) System.out.println("Command: " + args[0] +
			// " not found.");
			// }
			// if (command == null) {
			// System.out.println("Available commands are:");
			// System.out.println("");
			// for (Class c : classes) {
			// System.out.println(c.getSimpleName().toLowerCase());
			// }
			// System.out.println("Use <command> -? : to get the command help");
			// System.out.println("");
			// } else {
			// command.run(Arrays.copyOfRange(args, 1, args.length));
			// }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
