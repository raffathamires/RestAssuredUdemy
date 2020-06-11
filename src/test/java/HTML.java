import io.restassured.http.ContentType;
import org.junit.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class HTML {

    @Test
    public void deveFazerBuscaComHTML(){
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .rootPath("html.body.div.table.tbody")
                .body("tr.size()", is(3))
                .body("tr[1].td[2]", is("25"))
                .body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
        ;
    }

    @Test
    public void deveFazerBuscaComXPathEmHTML(){
        given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/v2/users?format=clean")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(hasXPath("count(//table/tr)",is("4")))
                .body(hasXPath("//td[text()='2']/../td[2]",is("Maria Joaquina")))
        ;
    }
}
