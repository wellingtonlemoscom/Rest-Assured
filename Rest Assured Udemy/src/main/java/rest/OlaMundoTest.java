package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);

		Assert.assertTrue("Status Code deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me/ola").then().statusCode(200);

		given()
				// Pré Condições
				.when()
				// Ação
				.get("http://restapi.wcaquino.me/ola").then()
				// Verificações
				.statusCode(200);
	}

	@Test
	public void devoConhecerMatchersHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria")); // verfica nomenclatura
		Assert.assertThat(128, Matchers.isA(Integer.class)); // verifica o tipo da variável
		Assert.assertThat(128, Matchers.greaterThan(127)); // verifica se valor é menor
		Assert.assertThat(128, Matchers.lessThan(129)); // verifica se valor é maior

		// Listas
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5)); // verifica tamanho da lista
		assertThat(impares, contains(1, 3, 5, 7, 9)); // verifica se os valores contém na ordem
		assertThat(impares, containsInAnyOrder(9, 3, 7, 5, 1)); // verifica se os valores contém sem ordem
		assertThat(impares, hasItem(1)); // verifica se um valor específico contém
		assertThat(impares, hasItems(9, 3, 7)); // verifica se n valores contém

		// Conectar várias assertivas dentro de uma lógica
		assertThat("Maria", is(not("João"))); // verifica a diferença de resposta
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("João"), is("Maria"))); // verifica se uma das respostas contém
		assertThat("Maria", allOf(startsWith("Ma"), endsWith("ia"), containsString("ri"))); // verifica partes da
																							// resposta de forma
																							// separada

	}

	@Test
	public void devoValidarBody() {
		given()
		.when()
				.get("http://restapi.wcaquino.me/ola")
		.then()
				.statusCode(200)
				.body(is("Ola Mundo!")) //verifica o texto completo
				.body(containsString("Mundo")) //verifica parte do texto
				.body(is(not(nullValue()))); //verifica se há um texto qualquer
	}
}
