/**
 * 
 */
package com.restitem.common.factory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restitem.common.api.dao.ItemDAO;
import com.restitem.common.api.factory.DAOFactory;
import com.restitem.common.dao.CMSItemDAO;
import com.restitem.common.dao.DBItemDAO;
import com.restitem.common.exception.StandardException;
import com.restitem.common.util.ResourceUtility;

/**
 * This class contains specialized contract details for operations related to
 * <code>Item</code>
 * 
 * @author gchandra
 * 
 */
@Service
public class ItemDAOFactory implements DAOFactory {

	private static final Logger LOG = Logger.getLogger(ItemDAOFactory.class);

	@Autowired
	private DBItemDAO dbItemDAO;

	@Autowired
	private CMSItemDAO cmsItemDAO;

	@Override
	public ItemDAO getItemDAO() {

		if (null == dbItemDAO && null == cmsItemDAO) {
			LOG.error("Data Source config error -> No Data Source configured.");
			throw new StandardException("500", "Internal System Error!");
		}

		//this config could be moved to spring context, configuration system or any property bundle
		return "db".equals(ResourceUtility.getProperty("dataSource")) ? dbItemDAO : cmsItemDAO;
	}

}
