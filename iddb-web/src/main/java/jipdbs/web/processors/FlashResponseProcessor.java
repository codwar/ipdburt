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

import jipdbs.web.Flash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.flow.ForceRedirect;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;

public abstract class FlashResponseProcessor extends ResponseProcessor {

	private static final Logger log = LoggerFactory.getLogger(FlashResponseProcessor.class);
	
	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		String resp = null;
		try {
			resp = processProcessor(context);
		} catch (ForceRedirect r) {
			throw r;
		} catch (Exception e) {
			log.error(e.getMessage());
			Flash.error(context.getRequest(), e.getMessage());
		} finally {
			context.getRequest().setAttribute("flash", Flash.clear(context.getRequest()));	
		}
		return resp;
	}
	
	public abstract String processProcessor(ResolverContext context) throws ProcessorException;

}
