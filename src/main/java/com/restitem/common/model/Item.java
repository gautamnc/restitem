/**
 * 
 */
package com.restitem.common.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class is the model entity for Item. This class could be used as a data
 * binding bean and/or persistence entity bean
 * 
 * @author gautamnc
 */
public class Item {

	private String itemId;
	private String description;
	private List<ItemInventory> inventory;
	private BigDecimal price;
	private BigDecimal discount;

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the inventory
	 */
	public List<ItemInventory> getInventory() {
		return inventory;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @param inventory
	 *            the inventory to set
	 */
	public void setInventory(List<ItemInventory> inventory) {
		this.inventory = inventory;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

}
