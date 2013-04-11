/**
 * 
 */
package com.restitem.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restitem.common.exception.StandardException;
import com.restitem.common.service.AuthenticationService;

/**
 * This is a convenience class to generate auth digest for subsequent rest calls
 * to server This class could serves as the handler for all authentication
 * related operations.
 * 
 * @author gautamnc
 * 
 */
@Controller
@RequestMapping(value = "/services/security")
public class AuthenticationController {
	@Autowired
	AuthenticationService authenticationService;
	
	private static final Logger LOG = Logger.getLogger(AuthenticationController.class);

	/**
	 * @param request http request received
	 * @param response http response to be sent
	 * @return auth digest which is a HmacSHA1 impl of public-private key encryption 
	 */
	@RequestMapping(value = "authToken", method = RequestMethod.GET)
	public @ResponseBody String getAuthDigest(HttpServletRequest request, HttpServletResponse response) {

		validateRequest(request);

		String digest = authenticationService.generateDigest(request.getHeader("appId"),
				request.getHeader("timestamp"), request.getHeader("uri"),
				RequestMethod.GET.toString());
		
		return new StringBuilder("{\"appIdKey\":\"").append(digest).append("\"}").toString();
	}
	
	
	@RequestMapping(value = "authToken")
	public @ResponseBody String getAuthDigestNoOp(HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> " + request.getRemoteAddr());
		throw new StandardException("403", "Method Not Supported Yet");
	}

	/**
	 * Validates the request headers and throws an exception with appropriate
	 * message
	 * 
	 * @param request
	 *            http request
	 */
	private void validateRequest(HttpServletRequest request) {
		if (null == request.getHeader("appId")
				|| null == request.getHeader("timestamp")
				|| null == request.getHeader("uri")
				|| request.getHeader("appId").length() == 0
				|| request.getHeader("timestamp").length() == 0
				|| request.getHeader("uri").length() == 0) {

			throw new StandardException("403", "Access Denied, Check the header fields.");
		} 
		
		try { 
			Double.parseDouble(request.getHeader("timestamp")); 
	    } catch(NumberFormatException e) { 
	    	throw new StandardException("403", "Access Denied, Check the header fields."); 
	    }
		
	}

}
