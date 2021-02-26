package com.wellingtonlemos.rest.test2.suite;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.wellingtonlemos.rest.core.BaseTest;
import com.wellingtonlemos.rest.test2.AuthTest;
import com.wellingtonlemos.rest.test2.ContasTest;
import com.wellingtonlemos.rest.test2.MovimentacaoTest;
import com.wellingtonlemos.rest.test2.SaldoTest;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest{
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wellingtonlemos@email.com");
		login.put("senha", "1234567");
		
		String TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token");
		
		requestSpecification.header("Authorization", "JWT " + TOKEN);
		
		get("/reset").then().statusCode(200);
	}
}
