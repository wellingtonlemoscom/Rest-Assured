package com.wellingtonlemos.rest.test2;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.wellingtonlemos.rest.core.BaseTest;
import com.wellingtonlemos.rest.test.utils.BarrigaUtils;

public class ContasTest extends BaseTest{
	
	@Test
	public void deveIncluirContatoComSucesso() {
		given()
			.body("{ \"nome\": \"Conta inserida\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveAlterarContatoComSucesso() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
			.body("{ \"nome\": \"Conta Alterada\" }")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.statusCode(200)
			.body("nome", is("Conta Alterada"))
		;
	}
	
	@Test
	public void naoDeveInserirContatoComMesmoNome() {
		given()
			.body("{ \"nome\": \"Conta mesmo nome\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
}
