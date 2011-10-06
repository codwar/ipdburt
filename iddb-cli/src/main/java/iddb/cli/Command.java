package iddb.cli;

import java.util.Arrays;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public abstract class Command {

	public void run(String[] args) throws Exception {
		OptionParser parser = getCommandOptions();
		parser.acceptsAll(Arrays.asList("h", "?"), "print help");
		
		OptionSet options = parser.parse(args);

		if (options.has("?")) {
			System.out.println("=== " + this.getClass().getSimpleName() + " ===\n");
			parser.printHelpOn(System.out);
			return;
		}
		execute(options);
	}

	/**
	 * Execute command
	 * 
	 * @param args
	 * @throws Exception
	 */
	protected abstract void execute(OptionSet options) throws Exception;
	
	protected abstract OptionParser getCommandOptions();
	
}
