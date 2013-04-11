/**
 * 
 */
package com.restitem.common.util;

import org.apache.log4j.Logger;

import com.restitem.common.exception.StandardException;

/**
 * This class is responsible for retrieving properties set for the application.
 * This class reads from properties bundle. This class could also be extended to
 * fetch properties from other resources over network or configuration
 * management systems over clusters.
 * 
 * @author gautamnc
 * 
 */
public class ResourceUtility {

	private static final Logger LOG = Logger.getLogger(ResourceUtility.class);

	/**
	 * This method returns value for the key passed in.
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		String prop = System.getenv(key);

		if (null == prop) {
			prop = fallback(key);
		}

		if (null == prop) {
			LOG.error("No environment variables set for -> " + key);
			throw new StandardException(
					"500",
					"Faulty environment variables. Please add -DdataSource=db (db OR cms) and  -DprivateKey=**** to the environment.");
		}

		return prop;
	}

	/**
	 * This is ultimate fall-back values for the system, for agreed behaviour
	 * across the consumers of the application. If spring config, external
	 * config sytems and JVM env variables fail to retreive values for critical
	 * configurations
	 * 
	 * @param key environment variable key
	 * @return property value
	 */
	private static String fallback(String key) {
		String prop = null;
		if (key.equals("dataSource")) {
			prop = "db";
		}
		if (key.equals("privateKey")) {
			prop = "supersecretprivatekey";
		}

		return prop;
	}
}
