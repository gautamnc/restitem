/**
 * 
 */
package com.restitem.common.api.factory;

import com.restitem.common.api.dao.ItemDAO;

/**
 * This interface serves as the contract for the DAO factory implementations on
 * the application
 * 
 * @author gchandra
 * 
 */
public interface DAOFactory {

	/**
	 * This method returns a concrete implementation of <code>ItemDAO</code>.
	 * The appropriate DAO implementation would be configured in system context
	 * xml, spring java config classes or from system environments
	 * 
	 * @return an impl of <code>ItemDAO</code>
	 */
	public ItemDAO getItemDAO();
}
