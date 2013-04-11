/**
 * 
 */
package com.restitem.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restitem.common.exception.StandardException;

/**
 * @author gchandra
 * 
 */
@Controller
@RequestMapping(value = "/services/system")
public class StatusController {

	private static final Logger LOG = Logger.getLogger(StatusController.class);

	@RequestMapping(value = "status", method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getSystemStatus(HttpServletRequest request) {
		LOG.info("Call received on status check -> " + request.getRemoteAddr());
		
		Map< String, Integer> mapResponse = new HashMap<String, Integer>();
		mapResponse.put("status", 0);
		
		return mapResponse;
	}

	@RequestMapping(value = "status")
	public String getSystemStatusFault(HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> "
				+ request.getRemoteAddr());
		throw new StandardException("403",
				"Method Not Supported for this operation");
	}

}
