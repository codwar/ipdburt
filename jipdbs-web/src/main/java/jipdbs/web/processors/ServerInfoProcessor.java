package jipdbs.web.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import jipdbs.core.JIPDBS;
import jipdbs.core.model.Server;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class ServerInfoProcessor extends FlashResponseProcessor {

	private static final Logger log = Logger.getLogger(ServerInfoProcessor.class.getName());
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();
		
		System.out.println(Arrays.toString(req.getParameterMap().keySet().toArray()));
		String[] keys = req.getParameterValues("key");

		List<Server> list = new ArrayList<Server>();
		for (String key : keys) {
			try {
				Server server = app.getServer(key);
				if (server.getDirty()) {
					app.refreshServerInfo(server);
				}
				list.add(server);
			} catch (EntityNotFoundException e) {
				log.severe(e.getMessage());
			}
		}
		
		req.setAttribute("list", list);
	
		return null;
	}

}
