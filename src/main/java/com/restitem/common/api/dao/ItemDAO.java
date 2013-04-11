/**
 * 
 */
package com.restitem.common.api.dao;

import com.restitem.common.model.Item;

/**
 * This class serves as the contract for all the DAO implementation related to
 * <code>Item</code>
 * 
 * @author gchandra
 * 
 */
public interface ItemDAO {

	/**
	 * This method returns the <code>Item</code> after a lookup/search from the
	 * data source.
	 * 
	 * @param itemId item id
	 * @return an instance of <code>Item</code> on success, else
	 *         <code>null</code>
	 */
	public Item getItem(String itemId, String locale);

	/**
	 * This method updates the item with the details passed in the instance of
	 * <code>Item</code>.
	 * 
	 * @param item an instance of <code>Item</code>
	 * @return on successful update, returns an instance of <code>Item</code>
	 *         else <code>null</code>
	 */
	public Item updateItem(Item item);

	/**
	 * This method creates a new item entry in data source with the details
	 * provided in the <code>Item</code> object passed in
	 * 
	 * @param item an instance of <code>Item</code>
	 * @return on successful commit, returns itemId of newly added item
	 */
	public String createItem(Item item);

	/**
	 * @param itemId item id
	 * @return <code>true</code> on successful deletion else returns
	 *         <code>false</code>
	 */
	public Boolean deleteItem(String itemId);

}
