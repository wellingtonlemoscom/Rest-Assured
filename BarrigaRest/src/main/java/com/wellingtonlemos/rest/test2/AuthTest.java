package com.wellingtonlemos.rest.test2;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

import org.junit.Test;

import com.wellingtonlemos.rest.core.BaseTest;

import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{
	
	@Test
	public void naoDeveAcessarAPISemToken() {	
		FilterableRequestSpecification req = (FilterableRequestSpecification) requestSpecification;
		req.removeHeader("Authorization");
		
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
}
