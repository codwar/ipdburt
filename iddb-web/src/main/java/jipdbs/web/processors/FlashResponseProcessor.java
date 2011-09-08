package jipdbs.web.processors;

import jipdbs.web.Flash;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public abstract class FlashResponseProcessor extends ResponseProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		String resp = processProcessor(context);
		context.getRequest().setAttribute("flash", Flash.clear(context.getRequest()));
		return resp;
	}
	
	public abstract String processProcessor(ResolverContext context) throws ProcessorException;

}
