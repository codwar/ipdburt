package jipdbs.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.util.Functions;

import com.google.appengine.api.utils.SystemProperty;

@SuppressWarnings("serial")
public class InfoServlet extends HttpServlet {

	public static class App {

		final String version;

		public App(String version) {
			this.version = version;
		}

		public String getVersion() {
			return version;
		}
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd.HHmm");
		String[] ver = SystemProperty.applicationVersion.get().split(".");
		Date d = new Date(Long.parseLong(ver[ver.length-1]));
		ver[ver.length-1] = format.format(d); 
		App app = new App(Functions.join(ver, "."));
		req.setAttribute("app", app);
	}
	
}
