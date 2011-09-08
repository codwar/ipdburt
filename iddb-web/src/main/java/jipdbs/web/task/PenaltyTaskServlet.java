package jipdbs.web.task;

import iddb.task.TaskManager;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.Context;
import jipdbs.web.JIPDBServlet;

public class PenaltyTaskServlet extends JIPDBServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(PenaltyTaskServlet.class.getName());
	
	private TaskManager task;

	@Override
	public void init() throws ServletException {
		this.task = (TaskManager) getServletContext().getAttribute(Context.TASK_MANAGER);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		String key = req.getParameter("key");
		String event = req.getParameter("event");

		log.fine("Processing " + key + " event " + event);

		this.task.processPenalty(key, event);

	}
}
