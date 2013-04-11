/**
 * 
 */
package com.restitem.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restitem.common.api.dao.ItemDAO;
import com.restitem.common.api.service.StandardService;
import com.restitem.common.exception.StandardException;
import com.restitem.common.factory.ItemDAOFactory;
import com.restitem.common.model.Item;

/**
 * This class acts as the service layer for operations related to Item. This
 * class is responsible for taking to DAO layer to fetch back the data for
 * presentation tier
 * 
 * @author gautamnc
 * 
 */
@Service
public class ItemService implements StandardService{
	
	@Autowired
	private ItemDAOFactory itemDAOFactory;
	
	/**
	 * This method retrieves back the details of the item talking to the data/persistence layer
	 * @param itemId item id
	 * @return
	 */
	public Item fetchItemDetails(String itemId, String locale) {
		return itemDAOFactory.getItemDAO().getItem(itemId, locale);
	}
	
	/**
	 * This method make class to appropriate persistence layer API to add new item in <code>Item</code> instance passed in.
	 * @param item an instance of <code>Item</code> to be added
	 * @return itemId of the newly added item
	 */
	public List<String> createNewItem(List<Item> items) {
		
		if (0 == items.size() || null == items) {
			throw new StandardException("500", "Provided Item list is not complete/valid to process the request.");
		}
		
		ItemDAO itemDAO = itemDAOFactory.getItemDAO();
		List<String> itemIds = new ArrayList<String>();
		
		for (Item item : items){ //iterating over the item bean instances and creating each at a time.
			itemIds.add(itemDAO.createItem(item));
		}
		return itemIds;
	}
}
