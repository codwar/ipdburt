package jipdbs.web;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jipdbs.core.JIPDBS;

public class JIPDBSContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		JIPDBS app = (JIPDBS) event.getServletContext().getAttribute("jipdbs");
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

		JIPDBS app = new JIPDBS(props);
		
		context.setAttribute("jipdbs", app);
	}
}
