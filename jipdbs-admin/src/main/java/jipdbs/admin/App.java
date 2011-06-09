package jipdbs.admin;

import java.io.IOException;
import java.util.Arrays;

public class App 
{
    public static void main( String[] args )
    {
    	Command command = null;
    	try {
			Class[] classes = Introspect.getClasses("jipdbs.admin.commands");
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
				System.out.println("");
			} else {
				command.run(Arrays.copyOfRange(args, 1, args.length));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
   
}
