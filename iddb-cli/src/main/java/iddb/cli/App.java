package iddb.cli;

import java.io.IOException;
import java.util.Arrays;

public class App 
{
    @SuppressWarnings("rawtypes")
	public static void main( String[] args )
    {
    	Command command = null;
    	try {
			Class[] classes = Introspect.getClasses("iddb.cli.command");
			if (args.length > 0) {
				for (Class c : classes) {
					if (args[0].toLowerCase().equals(c.getSimpleName().toLowerCase())) {
						command = (Command) c.newInstance();
						break;
					}
				}
				if (command == null) System.out.println("Command: " + args[0] + " not found.");
			}
			if (command == null) {
				System.out.println("Available commands are:");
				System.out.println("");
				for (Class c : classes) {
					System.out.println(c.getSimpleName().toLowerCase());
				}
				System.out.println("Use <command> -? : to get the command help");
				System.out.println("");
			} else {
				command.run(Arrays.copyOfRange(args, 1, args.length));
			}
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
