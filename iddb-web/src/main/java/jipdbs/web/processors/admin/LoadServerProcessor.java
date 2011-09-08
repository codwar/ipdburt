package jipdbs.web.processors.admin;

import iddb.core.JIPDBS;
import iddb.core.model.Server;

import java.util.logging.Logger;

import jipdbs.web.processors.ServerListProcessor;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class LoadServerProcessor extends ServerListProcessor {

	private static final Logger log = Logger.getLogger(LoadServerProcessor.class.getName());
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		String resp = super.doProcess(context);
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");

		Server server = null;
		try {
			server = app.getServer(context.getParameter("key"));
		} catch (EntityNotFoundException e) {
			log.severe(e.getMessage());
		}
		context.getRequest().setAttribute("server", server);	
		
		return resp;
	}

}
