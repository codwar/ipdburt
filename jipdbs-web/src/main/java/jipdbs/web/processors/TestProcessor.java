package jipdbs.web.processors;

import java.io.IOException;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public class TestProcessor extends ResponseProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		try {
			context.getResponse().getWriter().println(context.getParameter("key"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
