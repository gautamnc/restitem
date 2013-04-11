/**
 * 
 */
package com.restitem.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restitem.common.exception.StandardException;
import com.restitem.common.model.Item;
import com.restitem.common.service.SecurityService;
import com.restitem.common.service.ItemService;
import com.restitem.common.util.ResourceUtility;

/**
 * This class serves as the controller for all Item related activities in REST
 * Item application. This class hosts handlers for the Rest-ful services exposed
 * by the application.
 * 
 * @author gautamnc
 * 
 */
@Controller
@RequestMapping(value = "/services/items")
public class ItemController {

	@Autowired
	private SecurityService authenticationService;

	private static final Logger LOG = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	/**
	 * This handler returns the item details for the itemId passed in after
	 * proper authentication and request validation. Only GET method is
	 * supported for this operation.
	 * 
	 * @param itemId item id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public @ResponseBody Item itemDetails(@PathVariable String itemId, HttpServletRequest request) {

		authenticate(request);
		validateRequest(request);

		final Item item = itemService.fetchItemDetails(itemId, getLocale(request));//this could be an enum or geo code from location service
		if (null == item){
			throw new StandardException("404", "Not Found, Item not present in data store.");
		}
				
		return item;
	}

	/**
	 * This handler creates an item in the data source by making calls to service layer after proper request authentication.
	 * @param request http request received. POST method is supported here and find another delegator for PUT 
	 * <code>{@link #createItemsForPut(HttpServletRequest, HttpServletResponse, Item)}</code>
	 * 
	 * @param response http response to be sent
	 * @param A list of item instances of <code>Item</code> that needs to be created in data source
	 * @return A list of item ids of the newly added items 
	 */
	@RequestMapping(value="createItems", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody Map<String, List<String>> createItems(HttpServletRequest request,  HttpServletResponse response, @RequestBody List<Item> items) {
		
		authenticate(request);
		validateRequest(request, items);
		
		List<String> itemIds = itemService.createNewItem(items);
		
		Map< String, List<String>> mapResponse = new HashMap<String, List<String>>();
		mapResponse.put("itemIds", itemIds);
		
		return mapResponse;
	}
	
	private void validateRequest(HttpServletRequest request, List<Item> items) {
		if (null == items || 0 == items.size()) {
			LOG.error("Empty JSON item post data received -> " + request.getRemoteAddr());
			throw new StandardException("400", "Bad Request, JSON is not complete/valid to process the request.");
		}
		for (Item item : items){
			if (null == item.getItemId() || "".equals(item.getItemId())){
				LOG.error("Item post data without item Id received -> " + request.getRemoteAddr());
				throw new StandardException("400", "Bad Request, ItemId is mandatory for creating items.");
			}
		}
	}

	/**
	 * This handler creates an item in the data source by making calls to service layer after proper request authentication.
	 * @param request http request received. POST method is supported here and find another delegator for PUT 
	 * <code>{@link #createItems(HttpServletRequest, HttpServletResponse, Item)}</code>
	 * 
	 * @param response http response to be sent
	 * @param item instance/details of <code>Item</code> that needs to be created in data source
	 * @return item id of the newly added item
	 */
	@RequestMapping(value="createItems", method = RequestMethod.PUT, headers = "content-type=application/json")
	public @ResponseBody Map<String, List<String>> createItemsForPut(HttpServletRequest request,  HttpServletResponse response, @RequestBody List<Item> items) {
		return createItems(request, response, items);
	}
	
	/**
	 * Fault handler for inappropriate POST requests
	 * @param request http request received
	 * @param item 
	 */
	@RequestMapping(value="createItems", method = RequestMethod.POST)
	public @ResponseBody void createItemsFaultPost(HttpServletRequest request, @RequestBody Item item) {
		throw new StandardException("415", "Unsupported Type, inappropriate/unsupported POST data received.");
		
	}
	
	/**
	 * Fault handler for createItems operations
	 * 
	 * @param request http request received
	 */
	@RequestMapping(value="createItems")
	public void createItemsNoOp(HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> "
				+ request.getRemoteAddr());
		throw new StandardException("405", "Not Allowed, Method Not Supported for this operation");
	}

	@RequestMapping(value = "{itemId}")
	public void getItemDetailsNoOp(@PathVariable String itemId,
			HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> "+ request.getRemoteAddr());
		throw new StandardException("405", "Not Allowed, Method Not Supported for this operation");
	}

	/**
	 * This methof calls the service layer with request params/headers
	 * 
	 * @param request http request received from client
	 */
	private void authenticate(HttpServletRequest request) {

		int serverPort = request.getServerPort();
		
		StringBuilder landingUri = new StringBuilder(request.getScheme())
				.append("://").append(request.getServerName());
		if (80 != serverPort){ //ignoring default web port 80
			landingUri.append(":").append(request.getServerPort());
		}
		landingUri.append(request.getContextPath()) .append(request.getRequestURI().substring(request.getContextPath().length()));
		
		authenticationService.authenticate(request.getHeader("appIdKey"),
				request.getHeader("appId"), request.getHeader("timestamp"),
				landingUri.toString(), request.getMethod());
	}

	/**
	 * Validates the request headers and throws an exception with appropriate
	 * message
	 * 
	 * @param request http request received from client
	 */
	private void validateRequest(HttpServletRequest request) {
		if (null == request.getHeader("appIdKey")
				|| null == request.getHeader("appId")
				|| null == request.getHeader("timestamp")
				|| request.getHeader("appIdKey").trim().length() == 0
				|| request.getHeader("appId").trim().length() == 0
				|| request.getHeader("timestamp").trim().length() == 0) {

			throw new StandardException(
					"400", "Bad Request, Inappropriate POST headers received.");
		}
	}
	
	/**
	 * @param request http request received
	 * @return locale fetched from request or location service
	 */
	private String getLocale(HttpServletRequest request) {
		//locationService.getLocale(request); could rely on location service to get locale
		return ResourceUtility.getProperty("locale");//stubbing location look up
	}
}
