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
public class AuthenticationService {

	private static final Logger LOG = Logger.getLogger(AuthenticationService.class);
	
	public void authenticate(final String digest, final String appId,
			final String timestamp, final String uri, final String method) {

		if (!digest.equals(generateDigest(appId, timestamp, uri, method))) {
			LOG.error("Authentication Failed for -> " + new StringBuilder(appId).append(timestamp).append(uri).append(method).toString());
			throw new StandardException("403", "Access Denied, Authentication Failure!");
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
				.append(uri).append(method).toString().getBytes());

		return Hex.encodeHexString(rawdigest);
	}

}
