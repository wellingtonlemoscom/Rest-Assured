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
				// Pr� Condi��es
				.when()
				// A��o
				.get("http://restapi.wcaquino.me/ola").then()
				// Verifica��es
				.statusCode(200);
	}

	@Test
	public void devoConhecerMatchersHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria")); // verfica nomenclatura
		Assert.assertThat(128, Matchers.isA(Integer.class)); // verifica o tipo da vari�vel
		Assert.assertThat(128, Matchers.greaterThan(127)); // verifica se valor � menor
		Assert.assertThat(128, Matchers.lessThan(129)); // verifica se valor � maior

		// Listas
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5)); // verifica tamanho da lista
		assertThat(impares, contains(1, 3, 5, 7, 9)); // verifica se os valores cont�m na ordem
		assertThat(impares, containsInAnyOrder(9, 3, 7, 5, 1)); // verifica se os valores cont�m sem ordem
		assertThat(impares, hasItem(1)); // verifica se um valor espec�fico cont�m
		assertThat(impares, hasItems(9, 3, 7)); // verifica se n valores cont�m

		// Conectar v�rias assertivas dentro de uma l�gica
		assertThat("Maria", is(not("Jo�o"))); // verifica a diferen�a de resposta
		assertThat("Maria", not("Jo�o"));
		assertThat("Maria", anyOf(is("Jo�o"), is("Maria"))); // verifica se uma das respostas cont�m
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
				.body(is(not(nullValue()))); //verifica se h� um texto qualquer
	}
}
