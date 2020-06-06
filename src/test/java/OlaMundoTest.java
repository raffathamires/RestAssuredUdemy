import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OlaMundoTest {

    @Test
    public void testeOlaMundo() {
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O status code deveria ser 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured() {
        given(). //pre condicoes
        when(). //Acoes
                get("http://restapi.wcaquino.me/ola").
        then(). //Assertivas
                statusCode(200);
    }

    @Test
    public void devoConhecerMatchersHamcrest(){
        Assert.assertThat("Maria", Matchers.is("Maria"));
        Assert.assertThat(128, Matchers.isA(Integer.class));
        Assert.assertThat(128d, Matchers.isA(Double.class));
        Assert.assertThat(130, Matchers.greaterThan(128));
        Assert.assertThat(128, Matchers.lessThan(130));//menor que

        List<Integer> impares = Arrays.asList(1,3,5,7,9);
        Assert.assertThat(impares, Matchers.hasSize(5));//tamanho
        Assert.assertThat(impares, Matchers.contains(1,3,5,7,9));//contem todos elementos da lista
        Assert.assertThat(impares, Matchers.containsInAnyOrder(3,5,1,7,9));//todos elementos da lista em qualquer ordem
        Assert.assertThat(impares, Matchers.hasItem(7));//tem um item
        Assert.assertThat(impares, Matchers.hasItems(5,7,9));//tem mais de um item

        Assert.assertThat("Maria", Matchers.not("Joao"));
        Assert.assertThat("Maria", Matchers.anyOf(Matchers.is("Maria"), Matchers.is("Joaquina")));
        Assert.assertThat("Joaquina", Matchers.allOf(Matchers.startsWith("Joa"), Matchers.endsWith("ina")));
    }

    @Test
    public void validarBody() {
        given(). //pre condicoes
        when(). //Acoes
                get("http://restapi.wcaquino.me/ola").
        then(). //Assertivas
                statusCode(200).
                body(Matchers.is("Ola Mundo!")).
                body(Matchers.containsString("Mundo")).
                body(Matchers.is(Matchers.not(Matchers.nullValue())));
    }
}
