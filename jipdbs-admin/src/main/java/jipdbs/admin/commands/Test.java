package jipdbs.admin.commands;

import jipdbs.admin.Command;

public class Test extends Command {

	@Override
	protected void execute(String[] args) throws Exception {
		System.out.println("This is a dummy command");
	}

}
