/**
 * 
 */
package com.restitem.common.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.restitem.common.integrationtest.AbstractBase;

/**
 * @author gchandra
 * 
 */
public class GetItemDetailsTest extends AbstractBase {

	String timestamp = "12.00";
	String createItemUri = "http://localhost:7070/restitem/services/items/AAA101";
	String appId = "APP101";
	
	String jsonBody = "[{\"itemId\":\"AAA101\",\"description\":\"description-for-nik\",\"inventory\":"
			+ "[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"},"
			+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
			+ "\"price\":1299.99,\"discount\":300.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
			+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
			+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";

	String jsonBodyToModifyAAA101 = "[{\"itemId\":\"AAA101\",\"description\":\"modified\",\"inventory\":"
			+ "[{\"itemId\":null,\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\""
			+ ":\"South Bay\"}],\"price\":1299.99,\"discount\":300.49}]";

	String uriForHeader = RestAssured.baseURI + RestAssured.basePath
			+ "/security/authToken";

	@Test
	public void testModifyItem() { //covers get Item

		// creating AAA101 and AAA102
		Response responseForCreate = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", CREATE_ITEM_PUT_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(jsonBody)
				.when().put("/items/createItems");

		assertEquals(responseForCreate.getStatusCode(), 200);

		// modifying AAA101
		Response responseForModify = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json")
				.header(acceptJson)
				.body(jsonBodyToModifyAAA101)
				.when().post("/items/createItems");

		assertEquals(200, responseForModify.getStatusCode());

		// getting item AAA101
		Response response = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", GET_ITEM_DETAILS_GET_DIGEST)
				.header("Content-Type", "application/json")
				.header(acceptJson)
				.expect()
				.statusCode(200).log().ifError()
				.when().get("/items/AAA101");

		assertEquals(response.getStatusCode(), 200);

		JsonPath jsonPath = new JsonPath(response.asString());
		assertEquals("modified", jsonPath.get("description"));
	}
	
	@Test
	public void testIncorrectRestURI() {
		given().header("timestamp", timestamp).header("uri", uriForHeader)
				.header("appId", appId).header("method","GET").header(acceptJson)
			.expect().statusCode(404)
			.log().ifError()
			.when().get("/item&*&*s/AAA101");
	}
	
	@Test
	public void testIncorrectHeaderURI() {
		given().header("timestamp", timestamp).header("uri", uriForHeader)
				.header("appId", "@#$@#$").header("method","GET").header(acceptJson)
			.expect().statusCode(401)
			.log().ifError()
			.when().get("/items/AAA101");
	}
	
	@Test
	public void testNoHeaderURI() {
		given()
			.expect().statusCode(401)
			.log().ifError()
			.when().get("/items/AAA101");
	}

}
