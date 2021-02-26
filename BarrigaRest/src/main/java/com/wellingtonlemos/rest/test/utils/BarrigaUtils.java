package com.wellingtonlemos.rest.test.utils;

import static io.restassured.RestAssured.get;

import com.wellingtonlemos.rest.test.Movimentacao;

public class BarrigaUtils {
	public static Integer getIdContaPeloNome(String nome) {
		return get("/contas?nome="+nome).then().extract().path("id[0]");
	}
	
	public static Integer getIdContaPelaDescricao(String desc) {
		return get("/transacoes?descricao="+desc).then().extract().path("id[0]");
	}
	
	public static Movimentacao getMovimentacaoValido() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
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
