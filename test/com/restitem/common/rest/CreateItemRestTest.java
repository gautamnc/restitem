/**
 * 
 */
package com.restitem.common.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.restitem.common.integrationtest.AbstractBase;

/**
 * @author gchandra
 * 
 */
public class CreateItemRestTest extends AbstractBase {

	String timestamp = "12.00";
	String createItemUri = "http://localhost:7070/restitem/services/items/createItems";
	String appId = "APP101";
	String jsonBody = "[{\"itemId\":\"AAA101\",\"description\":\"description-for-nik\",\"inventory\":"
			+ "[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"},"
			+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
			+ "\"price\":1299.99,\"discount\":300.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
			+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
			+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";

	String jsonBodyToModifyAAA101 = "[{\"itemId\":\"AAA101\",\"description\":\"modified\",\"inventory\":"
			+ "[{\"quantity\":100,\"customerId\":\"-=modified=-\",\"storeId\":\"-=modified=-\",\"locale\":\"South Bay\"},"
			+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
			+ "\"price\":0.99,\"discount\":0.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
			+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
			+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";

	@Test
	public void testCreateItemPOST() {
		Response response = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(jsonBody).when().post("/items/createItems");

		assertEquals(response.getStatusCode(), 200);
		JsonPath jsonPath = new JsonPath(response.asString());
		assertEquals("[AAA101, AAA102]", jsonPath.get("itemIds").toString());
	}

	@Test
	public void testCreateItemPUT() {
		Response response = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", CREATE_ITEM_PUT_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(jsonBody).when().put("/items/createItems");

		assertEquals(response.getStatusCode(), 200);
		JsonPath jsonPath = new JsonPath(response.asString());
		assertEquals("[AAA101, AAA102]", jsonPath.get("itemIds").toString());
	}

	@Test
	public void testCreateItemWithSplCharsJSONKeyInPOSTBody() {
		// post body with spl chars in description
		String incorrectJSONBodyToModifyAAA101 = "[{\"itemId\":\"AAA101\",\"descri@#$#ption\":\"modified\",\"inventory\":"
				+ "[{\"quantity\":100,\"customerId\":\"-=modified=-\",\"storeId\":\"-=modified=-\",\"locale\":\"South Bay\"},"
				+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
				+ "\"price\":0.99,\"discount\":0.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
				+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
				+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";

		given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(incorrectJSONBodyToModifyAAA101).expect().statusCode(400)
				.when().post("/items/createItems");
	}

	@Test
	public void testCreateItemWithSplCharsJSONValuesInPOSTBody() {
		// post body with spl chars in description
		String incorrectJSONBodyToModifyAAA101 = "[{\"itemId\":\"AAA101\",\"description\":\"-=modified=-\",\"inventory\":"
				+ "[{\"quantity\":100,\"customerId\":\"-=modified=-\",\"storeId\":\"-=modified=-\",\"locale\":\"South Bay\"},"
				+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
				+ "\"price\":0.99,\"discount\":0.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
				+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
				+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";

		given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(incorrectJSONBodyToModifyAAA101).expect().statusCode(200)
				.when().post("/items/createItems");

		// getting item AAA101
		Response response = given().header("timestamp", timestamp)
				.header("uri", createItemUri).header("appId", appId)
				.header("appIdKey", GET_ITEM_DETAILS_GET_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.expect().statusCode(200).log().ifError().when()
				.get("/items/AAA101");

		assertEquals(response.getStatusCode(), 200);

		JsonPath jsonPath = new JsonPath(response.asString());
		assertEquals("-=modified=-", jsonPath.get("description"));
	}

	@Test
	public void testCreateItemWithEmptyJSONPOSTBody() {

		given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body("").expect().statusCode(400).when()
				.post("/items/createItems");

		given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.expect().statusCode(400).when()
				.post("/items/createItems");
	}
	
	@Test
	public void testCreateItemWithNoItemIdInJSONPOSTBody() {

		String jsonBodyWithNoItemId = "[{\"itemId\":\"\",\"description\":\"description-for-nik\",\"inventory\":"
				+ "[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"},"
				+ "{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],"
				+ "\"price\":1299.99,\"discount\":300.49},{\"itemId\":\"AAA102\",\"description\":\"description-for-nik\","
				+ "\"inventory\":[{\"quantity\":100,\"customerId\":\"150B0\",\"storeId\":\"STR01\",\"locale\":\"South Bay\"}"
				+ ",{\"quantity\":101,\"customerId\":\"150B1\",\"storeId\":\"STR02\",\"locale\":\"San Francisco\"}],\"price\":1299.99,\"discount\":300.49}]";
		
		given().header("timestamp", timestamp).header("uri", createItemUri)
				.header("appId", appId)
				.header("appIdKey", CREATE_ITEM_POST_DIGEST)
				.header("Content-Type", "application/json").header(acceptJson)
				.body(jsonBodyWithNoItemId).expect()
				.statusCode(400).when().log().ifError()
				.post("/items/createItems");
	}

}
