/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package jipdbs.web.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ProcessorContext;
import ar.sgt.resolver.processor.ResolverContext;

public abstract class SimpleActionProcessor implements Processor  {

	private static final Logger log = LoggerFactory.getLogger(SimpleActionProcessor.class);
	
	/* (non-Javadoc)
	 * @see ar.sgt.resolver.processor.Processor#process(ar.sgt.resolver.processor.ProcessorContext, ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public void process(ProcessorContext processorContext, ResolverContext context)
			throws ProcessorException {
		log.debug("Entering processor");
		String resp = doProcess(context);
		String redirect = resp != null ? resp : processorContext.getRedirect();
		log.debug("Redirect to {}", redirect);
		try {
			context.getResponse().sendRedirect(context.getResponse().encodeRedirectURL(context.getRequest().getContextPath() + redirect));
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new ProcessorException(e);
		}
	}
	
	public abstract String doProcess(ResolverContext context) throws ProcessorException;

}
