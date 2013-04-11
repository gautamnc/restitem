/**
 * 
 */
package com.restitem.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restitem.common.exception.StandardException;
import com.restitem.common.service.SecurityService;

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
public class SecurityController {
	@Autowired
	SecurityService authenticationService;
	
	private static final Logger LOG = Logger.getLogger(SecurityController.class);

	/**
	 * @param request http request received
	 * @param response http response to be sent
	 * @return auth digest which is a HmacSHA1 impl of public-private key encryption 
	 */
	@RequestMapping(value = "authToken", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getAuthDigest(HttpServletRequest request, HttpServletResponse response) {

		validateRequest(request);

		String digest = authenticationService.generateDigest(request.getHeader("appId"),
				request.getHeader("timestamp"), request.getHeader("uri"),
				request.getHeader("method"));
		
		Map< String, String> mapResponse = new HashMap<String, String>();
		mapResponse.put("appIdKey", digest);
		
		return mapResponse;
	}
	
	
	@RequestMapping(value = "authToken")
	public @ResponseBody void getAuthDigestFault(HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> " + request.getRemoteAddr());
		throw new StandardException("405", "Not Allowed, Method Not Supported for this operation");
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
				|| null == request.getHeader("method")
				|| request.getHeader("appId").length() == 0
				|| request.getHeader("timestamp").length() == 0
				|| request.getHeader("uri").length() == 0
				|| request.getHeader("method").length() == 0){

			throw new StandardException("400", "Bad Request, Check the header fields.");
		} 
		
		try { 
			Double.parseDouble(request.getHeader("timestamp")); 
	    } catch(NumberFormatException e) { 
	    	throw new StandardException("400", "Bad Request, Check the header fields."); 
	    }
		
	}

}
