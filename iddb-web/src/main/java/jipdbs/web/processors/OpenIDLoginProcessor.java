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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jipdbs.web.Flash;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.exception.ReverseException;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ProcessorContext;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.processor.ResponseProcessor;
import ar.sgt.resolver.utils.UrlReverse;

/**
 * @author 12072245
 * 
 */
public class OpenIDLoginProcessor implements Processor {

	private static final Logger log = LoggerFactory
			.getLogger(ResponseProcessor.class);

	private static final String OPTIONAL_VALUE = "0";
	private static final String REQUIRED_VALUE = "1";
	
	private String failUrl;
	private String successUrl;
	
	private ConsumerManager manager;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ar.sgt.resolver.processor.Processor#process(ar.sgt.resolver.processor
	 * .ProcessorContext, ar.sgt.resolver.processor.ResolverContext)
	 */
	@Override
	public void process(ProcessorContext processorContext,
			ResolverContext context) throws ProcessorException {

		HttpServletRequest req = context.getRequest();
		HttpServletResponse resp = context.getResponse();

		ProxyProperties proxyProps = new ProxyProperties();
		proxyProps.setProxyHostName("localhost");
		proxyProps.setProxyPort(3128);
		HttpClientFactory.setProxyProperties(proxyProps);
		
		UrlReverse reverse = new UrlReverse(context.getServletContext());
		try {
			this.failUrl = reverse.resolve("openid");
		} catch (RuleNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ReverseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//this.successUrl = StringUtils.isEmpty(context.getRequest().getParameter("next")) ? context.getRequest().getContextPath() : context.getRequest().getParameter("next");
		this.successUrl = StringUtils.isEmpty(processorContext.getRedirect()) ? context.getRequest().getContextPath() : processorContext.getRedirect();
		
		try {
			manager = new ConsumerManager();
			manager.setAssociations(new InMemoryConsumerAssociationStore());
			manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
			manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);

			if ("true".equals(req.getParameter("is_return"))) {
				processReturn(context);
			} else {
				String identifier = req.getParameter("openid_identifier");
				log.debug("Identifier {}", identifier);
				if (identifier != null) {
					this.authRequest(identifier, req, resp);
				} else {
					context.getServletContext().getRequestDispatcher(this.successUrl).forward(req, resp);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			Flash.error(req, e.getMessage());

			context.getRequest().setAttribute("flash", Flash.clear(context.getRequest()));
			
			try {
				context.getServletContext().getRequestDispatcher(this.successUrl).forward(context.getRequest(), context.getResponse());
			} catch (Exception e1) {
				log.error(e.getMessage());
				throw new ProcessorException(e);
			}
		}

	}

	private void processReturn(ResolverContext context)
			throws ServletException, IOException {
		
		HttpServletRequest req = context.getRequest();
		HttpServletResponse resp = context.getResponse();
		
		Identifier identifier = this.verifyResponse(req);
		
		log.debug("identifier: " + identifier);
		
		if (identifier == null) {
			context.getServletContext().getRequestDispatcher(this.failUrl).forward(req, resp);
		} else {
			req.setAttribute("identifier", identifier.getIdentifier());
			context.getServletContext().getRequestDispatcher(this.successUrl).forward(req, resp);
		}
		
	}

	// --- placing the authentication request ---
	public String authRequest(String userSuppliedString,
			HttpServletRequest httpReq, HttpServletResponse httpResp)
			throws IOException, ServletException {
		try {
			// configure the return_to URL where your application will receive
			// the authentication responses from the OpenID provider
			// String returnToUrl = "http://example.com/openid";
			String returnToUrl = "http://localhost:8080/iddb-web/openid/?is_return=true";

			// perform discovery on the user-supplied identifier
			List discoveries = manager.discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			httpReq.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			// Simple registration example
			addSimpleRegistrationToAuthRequest(httpReq, authReq);

			// Attribute exchange example
			addAttributeExchangeToAuthRequest(httpReq, authReq);

			httpResp.sendRedirect(authReq.getDestinationUrl(true));

//			if (!discovered.isVersion2()) {
//				// Option 1: GET HTTP-redirect to the OpenID Provider endpoint
//				// The only method supported in OpenID 1.x
//				// redirect-URL usually limited ~2048 bytes
//			} else {
//				// Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)
//				// TODO
//				//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/formredirection.jsp");
//				httpReq.setAttribute("prameterMap", httpReq.getParameterMap());
//				httpReq.setAttribute("message", authReq);
//				//dispatcher.forward(httpReq, httpResp);
//			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

		return null;
	}

	/**
	 * Simple Registration Extension example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a href="http://code.google.com/p/openid4java/wiki/SRegHowTo">Simple
	 *      Registration HowTo</a>
	 * @see <a
	 *      href="http://openid.net/specs/openid-simple-registration-extension-1_0.html">OpenID
	 *      Simple Registration Extension 1.0</a>
	 */
	private void addSimpleRegistrationToAuthRequest(HttpServletRequest httpReq,
			AuthRequest authReq) throws MessageException {
		// Attribute Exchange example: fetching the 'email' attribute
		// FetchRequest fetch = FetchRequest.createFetchRequest();
		SRegRequest sregReq = SRegRequest.createFetchRequest();

		String[] attributes = { "nickname", "email", "fullname", "dob",
				"gender", "postcode", "country", "language", "timezone" };
		for (int i = 0, l = attributes.length; i < l; i++) {
			String attribute = attributes[i];
			String value = httpReq.getParameter(attribute);
			if (OPTIONAL_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, false);
			} else if (REQUIRED_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, true);
			}
		}

		// attach the extension to the authentication request
		if (!sregReq.getAttributes().isEmpty()) {
			authReq.addExtension(sregReq);
		}
	}

	/**
	 * Attribute exchange example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a
	 *      href="http://code.google.com/p/openid4java/wiki/AttributeExchangeHowTo">Attribute
	 *      Exchange HowTo</a>
	 * @see <a
	 *      href="http://openid.net/specs/openid-attribute-exchange-1_0.html">OpenID
	 *      Attribute Exchange 1.0 - Final</a>
	 */
	private void addAttributeExchangeToAuthRequest(HttpServletRequest httpReq,
			AuthRequest authReq) throws MessageException {
		String[] aliases = httpReq.getParameterValues("alias");
		String[] typeUris = httpReq.getParameterValues("typeUri");
		String[] counts = httpReq.getParameterValues("count");
		FetchRequest fetch = FetchRequest.createFetchRequest();
		for (int i = 0, l = typeUris == null ? 0 : typeUris.length; i < l; i++) {
			String typeUri = typeUris[i];
			if (StringUtils.isNotBlank(typeUri)) {
				String alias = aliases[i];
				boolean required = httpReq.getParameter("required" + i) != null;
				int count = NumberUtils.toInt(counts[i], 1);
				fetch.addAttribute(alias, typeUri, required, count);
			}
		}
		authReq.addExtension(fetch);
	}

	// --- processing the authentication response ---
	public Identifier verifyResponse(HttpServletRequest httpReq)
			throws ServletException {
		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(
					httpReq.getParameterMap());

			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq
					.getSession().getAttribute("openid-disc");

			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = httpReq.getRequestURL();
			String queryString = httpReq.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(httpReq.getQueryString());

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(
					receivingURL.toString(), response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			log.debug("Verified {}", verified);
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification
						.getAuthResponse();

				receiveSimpleRegistration(httpReq, authSuccess);

				receiveAttributeExchange(httpReq, authSuccess);

				for (Iterator it = authSuccess.getExtensions().iterator(); it.hasNext() ; ) {
					Object o = it.next();
					log.debug(o.toString());
				}
				return verified; // success
			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

		return null;
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveSimpleRegistration(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension ext = authSuccess
					.getExtension(SRegMessage.OPENID_NS_SREG);
			if (ext instanceof SRegResponse) {
				SRegResponse sregResp = (SRegResponse) ext;
				for (Iterator iter = sregResp.getAttributeNames().iterator(); iter
						.hasNext();) {
					String name = (String) iter.next();
					String value = sregResp.getParameterValue(name);
					log.debug("Name {} value {}", name, value);
					httpReq.setAttribute(name, value);
				}
			}
		} else {
			log.debug("No extension");
		}
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveAttributeExchange(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
			FetchResponse fetchResp = (FetchResponse) authSuccess
					.getExtension(AxMessage.OPENID_NS_AX);

			// List emails = fetchResp.getAttributeValues("email");
			// String email = (String) emails.get(0);

			List aliases = fetchResp.getAttributeAliases();
			Map attributes = new LinkedHashMap();
			for (Iterator iter = aliases.iterator(); iter.hasNext();) {
				String alias = (String) iter.next();
				log.debug("Alias {}", alias);
				List values = fetchResp.getAttributeValues(alias);
				if (values.size() > 0) {
					String[] arr = new String[values.size()];
					values.toArray(arr);
					attributes.put(alias, StringUtils.join(arr));
				}
			}
			httpReq.setAttribute("attributes", attributes);
		} else {
			log.debug("NO AX EXTENSION");
		}
	}
}
