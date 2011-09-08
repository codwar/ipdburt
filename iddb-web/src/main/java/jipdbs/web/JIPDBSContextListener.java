package jipdbs.web;

import iddb.core.JIPDBS;
import iddb.task.TaskManager;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class JIPDBSContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		//JIPDBS app = (JIPDBS) event.getServletContext().getAttribute("jipdbs");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();

		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("jipdbs.properties"));
		} catch (Exception e) {
			context.log("Unable to load context properties: " + e.getMessage());
		}

		context.setAttribute(Context.JIPDBS, new JIPDBS(props));
		context.setAttribute(Context.TASK_MANAGER, new TaskManager());
	}
}
