package jipdbs.web.processors.admin;

import javax.servlet.http.HttpServletRequest;

import jipdbs.core.JIPDBS;
import jipdbs.core.util.GuidGenerator;
import jipdbs.web.Flash;

import org.datanucleus.util.StringUtils;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.RedirectProcessor;
import ar.sgt.resolver.processor.ResolverContext;

public class SaveServerProcessor extends RedirectProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String name = req.getParameter("name");
		String admin = req.getParameter("admin");
		String ip = req.getParameter("ip");

		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(admin)) {
			Flash.error(req, "Falta nombre o admin del server.");
			return null;
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
		
		return null;
	}

}
