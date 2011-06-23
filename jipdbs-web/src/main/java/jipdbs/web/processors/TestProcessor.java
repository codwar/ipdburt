package jipdbs.web.processors;

import java.io.IOException;

import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ResolverContext;

public class TestProcessor extends Processor {

	@Override
	public void doProcess(ResolverContext context) {
		try {
			context.getResponse().getWriter().println(context.getParameter("key"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
