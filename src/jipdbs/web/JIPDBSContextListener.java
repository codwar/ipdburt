package jipdbs.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
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

		ServletContext context = event.getServletContext();

		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("jipdbs.properties"));
		} catch (IOException e) {
			context.log("Unable to load context properties: " + e.getMessage());
		}

		JIPDBS app = new JIPDBS(props);

		app.start();
		context.setAttribute("jipdbs", app);
	}
}
