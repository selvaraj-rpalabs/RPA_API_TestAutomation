import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ContentExtraction {

    private static Response response;
    File file = new File("./src/test/testFile/test3_api.pdf");

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI="https://contentextraction-preprod.rpalabsinternal.com";
    }

    @Test(priority = 1)
    public void splitClassification(){
        RestAssured.basePath="/split/classification";
        response =
        given()
                .multiPart("file",file,"application/pdf")
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().all()
                .body("msg",equalTo("OK"))
                .and()
                .body("error",equalTo("false"))
                .extract().response();
    }

    @Test(priority = 2, dependsOnMethods = "splitClassification")
//    @AfterMethod
    public void extraction(){
        RestAssured.basePath = "/extract/auto";
        given()
                .contentType(ContentType.JSON)
                .body(response.asString())
                .log().all()
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().all()
                .and()
                .body("msg",equalTo("OK"))
                .body("error",equalTo("false"))
                .body("result[0].extraction_type",equalTo("bol"));

    }
}
