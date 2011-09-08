package jipdbs.web.processors;

import iddb.core.JIPDBS;
import iddb.core.model.Server;

import java.util.List;
import java.util.logging.Logger;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class ServerListProcessor extends FlashResponseProcessor {

	private static final Logger log = Logger.getLogger(ServerListProcessor.class
			.getName());
	
	public static final int SERVER_LIMIT = 50;
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {

		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		int[] count = new int[1];
		List<Server> servers = app.getServers(0, SERVER_LIMIT, count);
		
		log.fine("Listing " + servers.size() + " servers");
		log.finest(">>>> COUNT " + count[0]);
		
		context.getRequest().setAttribute("servers", servers);
		context.getRequest().setAttribute("count", count[0]);
		
		return null;
	}

}
