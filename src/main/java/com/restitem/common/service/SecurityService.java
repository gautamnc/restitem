/**
 * 
 */
package com.restitem.common.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.restitem.common.exception.StandardException;
import com.restitem.common.util.ResourceUtility;

/**
 * This class is responsible for all authentication related services for the
 * application
 * 
 * @author gautamnc
 * 
 */
@Service
public class SecurityService {

	private static final Logger LOG = Logger.getLogger(SecurityService.class);
	
	/**
	 * This method authenticate details fed in to the system, by comparing digest generated 
	 * with agreed private key and shared data and public key
	 * 
	 * @param digest digest string which is a HmacSHA1 impl of public-private key encryption
	 * @param appId app id
	 * @param timestamp timestamp
	 * @param uri uri attempted
	 * @param method http method of access
	 */
	public void authenticate(final String digest, final String appId, final String timestamp, final String uri, final String method) {
		
		validateRequest(digest, appId, timestamp, uri, method);
		
		if (!digest.equals(generateDigest(appId, timestamp, uri, method))) {
			LOG.error("Authentication Failed for -> " + new StringBuilder(appId).append(timestamp).append(uri).append(method).toString());
			throw new StandardException("401", "Unauthorized, Authentication Failed!");
		}
	}

	/**
	 * This method generates the digest for the provided information from the
	 * client.
	 * 
	 * @param timestamp timestamp send by client
	 * @param uri uri attempted
	 * @param method method of access
	 * @return digest string which is a HmacSHA1 impl of public-private key encryption
	 */
	public String generateDigest(final String appId, final String timestamp,
			final String uri, final String method) {

		String appIdProp = ResourceUtility.getProperty("privateKey");
		
		SecretKeySpec secretKeySpec = new SecretKeySpec(appIdProp.getBytes(), "HmacSHA1");

		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Digest Generation Failed ->" + e.getMessage());
			throw new StandardException("500", "Internal Server Error!");
		}
		try {
			mac.init(secretKeySpec);
		} catch (InvalidKeyException e) {
			LOG.error("Digest Generation Failed ->" + e.getMessage());
			throw new StandardException("500", "Internal Server Error!");
		}
		byte[] rawdigest = mac.doFinal(new StringBuilder(appId).append(timestamp)
				.append(uri).append(method.toUpperCase()).toString().getBytes());

		return Hex.encodeHexString(rawdigest);
	}
	
	/**
	 * Validates the request headers and throws an exception with appropriate
	 * message
	 * @param method http request method
	 * @param uri uri attempted 
	 * @param timestamp timestamp
	 * @param appId appid
	 * @param digest digest string which is a HmacSHA1 impl of public-private key encryption
	 */
	private void validateRequest(String digest, String appId, String timestamp, String uri, String method) {
		if (null == digest || "".equals(digest.trim())){
			LOG.error("Need Authentication digest to proceed -> appIdKey");
			throw new StandardException("401", "Unauthorized, Need appIdKey digest to process request");
		}
		
		if (null == digest
				|| null == appId
				|| null == timestamp
				|| digest.trim().length() == 0 || appId.trim().length() == 0
				|| timestamp.trim().length() == 0) {

			throw new StandardException(
					"400", "Bad Request, Inappropriate header data received");
		}
	}
}
