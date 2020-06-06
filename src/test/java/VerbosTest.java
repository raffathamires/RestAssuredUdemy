import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

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
                .body("error",is("Name é um atributo obrigatório"))
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