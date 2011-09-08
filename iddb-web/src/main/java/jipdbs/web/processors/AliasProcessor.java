package jipdbs.web.processors;

import iddb.core.JIPDBS;
import iddb.info.AliasResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import jipdbs.web.CommonConstants;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;

public class AliasProcessor extends FlashResponseProcessor {

	private static final Logger log = Logger.getLogger(AliasProcessor.class.getName());
	
	@Override
	public String processProcessor(ResolverContext context) throws ProcessorException {
		
		JIPDBS app = (JIPDBS) context.getServletContext().getAttribute("jipdbs");
		
		HttpServletRequest req = context.getRequest();

		String key = context.getParameter("key");
		
		int page = 1;
		try {
			page = Integer.parseInt(req.getParameter("o"));
			if (page <= 0) page = 1;
		} catch (NumberFormatException e) {
		}
		
		int pageSize = CommonConstants.DEFAULT_AJAX_PAGE_LIMIT;
		int offset = (page - 1) * pageSize;

		List<AliasResult> list = new ArrayList<AliasResult>();

		int[] count = new int[1];
		if (context.hasParameter("ip")) {
			log.fine("Alias IP");
			list = app.aliasip(key, offset, pageSize, count);
		} else {
			log.fine("Alias Name");
			list = app.alias(key, offset, pageSize, count);	
		}

		req.setAttribute("hasMore", new Boolean((offset + pageSize) < count[0]));
		req.setAttribute("list", list);
		req.setAttribute("total", count[0]);
		req.setAttribute("pages", (int) Math.ceil((double) count[0] / pageSize));
		req.setAttribute("offset", page);
	
		return null;
	}

}
