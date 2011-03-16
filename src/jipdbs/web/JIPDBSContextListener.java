package jipdbs.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jipdbs.JIPDBS;

public class JIPDBSContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		JIPDBS app = (JIPDBS) event.getServletContext().getAttribute("jipdbs");
		app.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		JIPDBS app = new JIPDBS();

		app.start();
		event.getServletContext().setAttribute("jipdbs", app);
	}
}
