/**
 * 
 */
package com.restitem.common.integrationtest;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Before;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;

/**
 * General set-up for testing
 * @author gchandra
 *
 */
public abstract class AbstractBase {
	
    static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/xml";
    private static final String TEXT_HTML = "text/html";

    protected static Header acceptJson = new Header("Accept", APPLICATION_JSON);
    protected static Header acceptXml = new Header("Accept", APPLICATION_XML);
    protected static Header acceptHtml = new Header("Accept", TEXT_HTML);
	
    final protected static String CREATE_ITEM_POST_DIGEST = "4a25123af13a8d8d5cc7d597d29d27bae9da7aab";
    final protected static String CREATE_ITEM_PUT_DIGEST ="cca3a73fff9b986b4864f0ffe9c7d9c073d86b3c";
    final protected static String GET_ITEM_DETAILS_GET_DIGEST = "d5c66fe14a28a8641e0ab6f21567624fd6ed05a9";

	/**
	 * Check the status of system before running specialized test cases
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		RestAssured.baseURI = "http://" + System.getProperty("restServer","localhost")  ;
        RestAssured.port = 7070;
        
        if (RestAssured.baseURI.contains("localhost")){
        	RestAssured.basePath = "/restitem/services/"; //because mostly local deploys are in app specific folders
        }else {
        	RestAssured.basePath = "/services/";
        }

        given()
            .expect().statusCode(200)
            .log().ifError()
            .when()
			.get("/system/status");
	}

}
