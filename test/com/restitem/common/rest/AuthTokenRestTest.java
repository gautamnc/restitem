/**
 * 
 */
package com.restitem.common.rest;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.restitem.common.integrationtest.AbstractBase;

/**
 * @author gchandra
 * 
 */
public class AuthTokenRestTest extends AbstractBase{

	String timestamp = "12.00";
	String createItemUri = "http://localhost:7070/restitem/services/items/createItems";
	String appId = "APP101";
	
	String uriForHeader = RestAssured.baseURI + RestAssured.basePath + "/security/authToken";

	/**
	 * Test HTTP 200 on auth service
	 */
	@Test
	public void testHttp200() {
		
		given().header("timestamp", timestamp).header("uri", uriForHeader)
				.header("appId", appId).header("method","GET").header(acceptJson)
			.expect().statusCode(200)
			.log().ifError()
			.when().get("/security/authToken");
	}
	
	@Test
	public void testDigest() {
		Response response = given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId).header("method","GET").header(acceptJson)
				.expect().statusCode(200)
				.log().ifError()
			.when().get("/security/authToken");
		
		assertEquals(200, response.getStatusCode());
        JsonPath jsonPath = new JsonPath(response.asString());
        assertEquals("14a03397df52b3f4f8c24e16575ec7300cc37bed", jsonPath.get("appIdKey"));
	}
}
