package jipdbs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datanucleus.util.StringUtils;

import jipdbs.JIPDBS;
import jipdbs.util.GuidGenerator;

public class ServerSaveServlet extends HttpServlet {

	private static final long serialVersionUID = 9193574096825280151L;

	private JIPDBS app;

	@Override
	public void init() throws ServletException {
		app = (JIPDBS) getServletContext().getAttribute("jipdbs");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String name = req.getParameter("name");
		String admin = req.getParameter("admin");
		String ip = req.getParameter("ip");

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(admin)) {
			Flash.error(req, "Falta nombre o admin del server.");
			return;
		}

		if (StringUtils.isEmpty(ip))
			Flash.warn(req, "No se indicó dirección IP. No se realizará comprobación del origen de los datos.");

		if (StringUtils.isEmpty(req.getParameter("k"))) {
			String uid = GuidGenerator.generate(name);
			app.addServer(name, admin, uid, ip);
			Flash.info(req, "Server agregado.");
		} else {
			app.saveServer(req.getParameter("k"), name, admin, ip);
			Flash.info(req, "Server editado.");
		}
	}
}
