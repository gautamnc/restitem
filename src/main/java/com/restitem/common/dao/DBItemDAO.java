/**
 * 
 */
package com.restitem.common.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.restitem.common.api.dao.ItemDAO;
import com.restitem.common.exception.StandardException;
import com.restitem.common.model.Item;
import com.restitem.common.model.ItemInventory;

/**
 * This class is responsible for all data base related operations on
 * <code>Item</code>.
 * 
 * @author gchandra
 */
@Component
public class DBItemDAO implements ItemDAO {

	private static final Logger LOG = Logger.getLogger(DBItemDAO.class);
	
	@Override
	public Item getItem(final String itemId, final String locale) {
		if (null == itemId || "".equals(itemId)){
			LOG.error("Item bean is null or itemId is not present in the bean");
			throw new StandardException("415", "Item Bean is not valid, Please check if itemId is present");
		}
		
		Item item = items.get(itemId);
		
		if (null != item){
			List<ItemInventory> itemInventories = new CopyOnWriteArrayList<ItemInventory>(item.getInventory());
			for (ItemInventory itemInventory : item.getInventory()){
				if (!itemInventory.getLocale().equals(locale)) {
					itemInventories.remove(itemInventory);
				}
			}
			item.setInventory(itemInventories);
		}
		return item;
	}

	@Override
	public String createItem(final Item item) { 
		
		String itemId = item.getItemId();
		
		if (null == item || null == itemId || "".equals(itemId)){
			LOG.error("Item bean is null or itemId is not present in the bean");
			throw new StandardException("415", "Item Bean is not valid, Please check if itemId is present");
		}
		
		items.put(itemId, item); //adding item to an in-memory data container
		return itemId;
	}

	@Override
	public Item updateItem(final Item item) {
		LOG.error("Opertaion not yet supported -> updateItem(Item)");
		throw new StandardException("404", "Not Found, Operation not yet supported!");
	}

	@Override
	public Boolean deleteItem(final String itemId) {
		LOG.error("Opertaion not yet supported -> deleteItem(String)");
		throw new StandardException("404", "Not Found, Operation not yet supported!");
	}
	
	/**
	 * In-memory data storage
	 */
	private Map<String, Item> items;
	
	
	/**
	 * Default constructor, populating in-memory data container
	 */
	DBItemDAO(){
		items = new HashMap<String, Item>();
		
		Item item = new Item();
		item.setItemId("NIK101");
		item.setDescription("description-for-nik");
		item.setDiscount(new BigDecimal(300.49));
		item.setPrice(new BigDecimal(1299.99));

		List<ItemInventory> itemInventories = new ArrayList<ItemInventory>();
		for (int i = 0; i < 5; i++) {
			ItemInventory itemInventory = new ItemInventory();
			itemInventory.setCustomerId("150B"+i);
			itemInventory.setItemId("NIK101");
			itemInventory.setQuantity(100 + i);
			itemInventory.setStoreId("STR01");
			itemInventory.setLocale("South Bay");
			itemInventories.add(itemInventory);
		}
		item.setInventory(itemInventories);
		
		items.put("NIK101", item);
		
		Item item2 = new Item();
		
		item2.setItemId("CAN101");
		item2.setDescription("description-for-can");
		item2.setDiscount(new BigDecimal(233.99));
		item2.setPrice(new BigDecimal(1199.99));

		List<ItemInventory>  itemInventories2 = new ArrayList<ItemInventory>();
		for (int i = 0; i < 5; i++) {
			ItemInventory itemInventory = new ItemInventory();
			itemInventory.setCustomerId("140A"+i);
			itemInventory.setItemId("CAN101");
			itemInventory.setStoreId("STR02");
			itemInventory.setLocale("San Francisco");
			itemInventory.setQuantity(10 + i);
			itemInventories2.add(itemInventory);
		}
		item.setInventory(itemInventories);
		items.put("CAN101", item2);
		
		Item item3 = new Item();
		
		item3.setItemId("RDM101");
		item3.setDescription("description-for-can");
		item3.setDiscount(new BigDecimal(233.99));
		item3.setPrice(new BigDecimal(1199.99));

		List<ItemInventory>  itemInventories3 = new ArrayList<ItemInventory>();
		for (int i = 0; i < 5; i++) {
			ItemInventory itemInventory = new ItemInventory();
			itemInventory.setCustomerId("140A"+i);
			itemInventory.setItemId("RDM101");
			itemInventory.setStoreId("STR01");
			itemInventory.setLocale("South Bay");
			itemInventory.setQuantity(10 + i);
			itemInventories3.add(itemInventory);
		}
		item.setInventory(itemInventories);
		items.put("RDM101", item3);
	}
}
