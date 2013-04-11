/**
 * 
 */
package com.restitem.common.controller;

import java.util.List;
import java.util.ListIterator;

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
import com.restitem.common.service.AuthenticationService;
import com.restitem.common.service.ItemService;

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
	private AuthenticationService authenticationService;

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
	public @ResponseBody Item getItemDetails(@PathVariable String itemId, HttpServletRequest request) {

		validateRequest(request);
		authenticate(request);

		final Item item = itemService.fetchItemDetails(itemId, getLocale(request));//this could be an enum or geo code from location service
		if (null == item){
			throw new StandardException("404", "Resource not found in data store.");
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
	 * @return A list of item ids of the newly added items, returns -1 incase of failure at index location 
	 */
	@RequestMapping(value="createItems", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody String createItems(HttpServletRequest request,  HttpServletResponse response, @RequestBody List<Item> items) {
		
		//TODO syntactic errors in json
		
		authenticate(request);
		
		if (null == items || 0 == items.size()) {
			LOG.error("Empty JSON post data or Item without item Id received -> " + request.getRemoteAddr());
			throw new StandardException("500", "POST data not complete/valid to process the request.");
		}
		
		List<String> itemIds = itemService.createNewItem(items);
		
		ListIterator<String> iterator = itemIds.listIterator();
		while (iterator.hasNext()){
			String itemId = iterator.next();
			if (null == itemId || "".equals(itemId)){
				iterator.set("-1");
			}
		}
		
		return new StringBuilder("{\"itemIds:\"").append(itemIds).append("\"}").toString();
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
	public @ResponseBody String createItemsForPut(HttpServletRequest request,  HttpServletResponse response, @RequestBody List<Item> items) {
		return createItems(request, response, items);
	}
	
	/**
	 * Fault handler for inappropriate POST requests
	 * @param request http request received
	 * @param item 
	 */
	@RequestMapping(value="createItems", method = RequestMethod.POST)
	public @ResponseBody void createItemsFaultPost(HttpServletRequest request, @RequestBody Item item) {
		throw new StandardException("415", "Inappropriate POST data received.");
		
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
		throw new StandardException("403", "Method Not Supported for this operation");
	}

	@RequestMapping(value = "{itemId}")
	public void getItemDetailsNoOp(@PathVariable String itemId,
			HttpServletRequest request) {
		LOG.error("Call received on method not supported yet -> "
				+ request.getRemoteAddr());
		throw new StandardException("403", "Method Not Supported for this operation");
	}

	/**
	 * This methof calls the service layer with request params/headers
	 * 
	 * @param request http request received from client
	 */
	private void authenticate(HttpServletRequest request) {

		String landingUri = new StringBuilder(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(":")
				.append(request.getServerPort())
				.append(request.getContextPath())
				.append(request.getRequestURI().substring(
						request.getContextPath().length())).toString();

		authenticationService.authenticate(request.getHeader("appIdKey"),
				request.getHeader("appId"), request.getHeader("timestamp"),
				landingUri, RequestMethod.GET.toString());
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
				|| request.getHeader("appIdKey").length() == 0
				|| request.getHeader("appId").length() == 0
				|| request.getHeader("timestamp").length() == 0) {

			throw new StandardException(
					"415", "Inappropriate POST headers received.");
		}
	}
	
	/**
	 * @param request http request received
	 * @return locale fetched from request or location service
	 */
	private String getLocale(HttpServletRequest request) {
		
		//locationService.getLocale(request); could rely on location service to get locale
		
		return "South Bay"; //hard coding for now
	}
}
