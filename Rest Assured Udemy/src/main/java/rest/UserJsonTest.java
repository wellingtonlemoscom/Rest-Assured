package rest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.allOf;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1)) //verificar id igual a 1
			.body("name", containsString("Silva")) //verifica se cont�m Silva no nome
			.body("age", greaterThan(18)) //Verifica idade maior que 18
		;
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		//path
		Assert.assertEquals(Integer.valueOf(1), response.path("id"));
		
		//jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
		
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void deveVerificarUmaLista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
	}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usu�rio inexistente"))
		;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("Jo�o da Silva", "Maria Joaquina", "Ana J�lia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))) //verifica uma lista que cont�m Zezinho e Luizinho
			.body("salary", contains(1234.5678f, 2500, null))
		;
	}
	
	@Test
	public void devoFazerVerificacoesAvancadas() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2)) //busca todos as idades menor ou igual a 25
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) //verifica de baixo para cima
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")) //verifica texto vindos em caixa alto
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", Matchers.anyOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			.body("id.max()", is(3)) //verifica o id m�ximo
			.body("salary.min()", is(1234.5678f)) //verifica menor salario
			.body("salary.findAll{it != null}sum()", is(closeTo(3734.5678f, 0.001))) //soma os salario; verificando as casas decimais
			.body("salary.findAll{it != null}sum()", allOf(greaterThan(3000d), lessThan(5000d))) //soma os salario; verificando um range de valores
		
		;
	}
	
	@Test
	public void devoUnirJsonPathComJava() {
		ArrayList<String> names = 
			given()
			.when()
				.get("http://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}")
					
				;
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina"));
		Assert.assertEquals(names.get(0).toUpperCase(), "Maria Joaquina".toUpperCase());
	}
}
