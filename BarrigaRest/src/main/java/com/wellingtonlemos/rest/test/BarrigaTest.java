package com.wellingtonlemos.rest.test;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.wellingtonlemos.rest.core.BaseTest;
import com.wellingtonlemos.rest.test.utils.DataUtils;

import io.restassured.specification.FilterableRequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest{
	
	private static String CONTA_NAME = "Conta " + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
	@BeforeClass
	public void login() {
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
	}
	
	@Test
	public void deveIncluirContatoComSucesso() {
		CONTA_ID = given()
			.body("{ \"nome\": \""+ CONTA_NAME + "\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void deveAlterarContatoComSucesso() {	
		given()
			.body("{ \"nome\": \"" + CONTA_NAME + " Alterada\" }")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.statusCode(200)
			.body("nome", is(CONTA_NAME + " Alterada"))
		;
	}
	
	@Test
	public void naoDeveInserirContatoComMesmoNome() {
		given()
			.body("{ \"nome\": \"" + CONTA_NAME + " Alterada\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void deveInserirMovimentacaoSucesso() {
		Movimentacao mov = getMovimentacaoValido();
		
		MOV_ID = given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
		given()
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
		;
	}
	
	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValido();
		mov.setData_transacao(DataUtils.getdataDiferenciaDias(2));
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == " + CONTA_ID + "}.saldo", is("100.00"))
		;
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.statusCode(204)
		;
	}
	
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
	
	private Movimentacao getMovimentacaoValido() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
		mov.setDescricao("Descricao da movimentacao");
		mov.setEnvolvido("Envolvido no mov");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getdataDiferenciaDias(-1));
		mov.setData_pagamento(DataUtils.getdataDiferenciaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
}


