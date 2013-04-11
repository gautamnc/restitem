/**
 * 
 */
package com.restitem.common.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.restitem.common.api.dao.ItemDAO;
import com.restitem.common.exception.StandardException;
import com.restitem.common.model.Item;

/**
 * This class is responsible for all CMS/EMS realted operations for
 * <code>Item</code>. This class would make REST/SOAP/FTP/Direct CMS API read to
 * get data across the wire.
 * 
 * For time being, this class reads JSON data from a file.
 * 
 * @author gchandra
 * 
 */
@Component
public class CMSItemDAO implements ItemDAO {

	private static final Logger LOG = Logger.getLogger(CMSItemDAO.class);
	
	@Override
	public Item getItem(String itemId,final String locale) {
		LOG.error("Opertaion not yet supported -> getItem(Item, String)");
		throw new StandardException("501", "Not Implemented, Operation not yet supported!");
	}

	@Override
	public String createItem(Item item) {
		LOG.error("Opertaion not yet supported -> createItem(Item)");
		throw new StandardException("501", "Not Implemented, Operation not yet supported!");
	}
	
	@Override
	public Item updateItem(Item item) {
		LOG.error("Opertaion not yet supported -> updateItem(Item)");
		throw new StandardException("501", "Not Implemented, Operation not yet supported!");
	}

	@Override
	public Boolean deleteItem(String itemId) {
		LOG.error("Opertaion not yet supported -> deleteItem(String)");
		throw new StandardException("501", "Not Implemented, Operation not yet supported!");
	}

}
