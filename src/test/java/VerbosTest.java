import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerbosTest {

    @BeforeClass
    public static void setup() {
        baseURI = "http://restapi.wcaquino.me";
    }

    @Test
    public void deveSalvarUsuario() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"José Santos\",\"age\":38,\"salary\":4300}")
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("José Santos"))
                .body("age", is(38))
                .body("salary", is(4300))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "José Santos");
        params.put("age", 38);
        params.put("salary", 4300);

        given()
                .log().all()
                .contentType("application/json")
                .body(params)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("José Santos"))
                .body("age", is(38))
                .body("salary", is(4300))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto() {
        User user = new User("José Santos", 38);

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("José Santos"))
                .body("age", is(38))
        ;
    }

    @Test
    public void deveDeserializarAoSalvarUsuario() {
        User user = new User("José Santos", 38);

        User usuarioRetorno = given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;

        System.out.println(usuarioRetorno);
        Assert.assertThat(usuarioRetorno.getId(), notNullValue());
        Assert.assertEquals("José Santos", usuarioRetorno.getName());
        Assert.assertThat(usuarioRetorno.getAge(),is(38));

    }

    @Test
    public void naoDeveSalvaUsuarioSemNome() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"age\":28,\"salary\":4700}")
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioXML() {
        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Jose Santos</name><age>38</age><salary>4300</salary></user>")
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Jose Santos"))
                .body("user.age", is("38"))
                .body("user.salary", is("4300"))
        ;
    }

    @Test
    public void deveSalvarUsuarioXMviaObjeto() {
        User user = new User("Jose Santos", 38);

        given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Jose Santos"))
                .body("user.age", is("38"))
        ;
    }

    @Test
    public void deveDeseralizarUsuarioUsandoXML() {
        User user = new User("Jose Santos", 38);

        User usuarioRetorno = given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;
        Assert.assertThat(usuarioRetorno.getId(), notNullValue());
        Assert.assertThat(usuarioRetorno.getName(), is("Jose Santos"));
        Assert.assertThat(usuarioRetorno.getAge(), is(38));
    }

    @Test
    public void deveAlterarUsuario() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Usuario Alterado\",\"age\":80,\"salary\":5000}")
                .when()
                .put("/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuario Alterado"))
                .body("age", is(80))
                .body("salary", is(5000))
        ;
    }

    @Test
    public void devoCustomizarURL() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Usuario Alterado\",\"age\":80,\"salary\":5000}")
                .pathParam("entidade", "users")
                .pathParam("userID", 1)
                .when()
                .put("/{entidade}/{userID}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuario Alterado"))
                .body("age", is(80))
                .body("salary", is(5000))
        ;
    }

    @Test
    public void deveRemoverUsuario() {
        given()
                .log().all()
                .when()
                .delete("/users/1")
                .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void naoDeveRemoverUsuarioInexistente() {
        given()
                .log().all()
                .when()
                .delete("/users/5")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }
}