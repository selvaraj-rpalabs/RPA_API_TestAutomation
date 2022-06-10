import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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

public class ContentExtraction extends Base{

    private static Response response;
    File file = new File("./src/test/testFile/test3_api.pdf");

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI="https://contentextraction-preprod.rpalabsinternal.com";
    }

    @Test(priority = 1)
    public void splitClassification(){
        ExtentTest extentTest = extent.createTest("Content Extraction");
        extentTest.log(Status.INFO, "Split Classification Started");
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
        extentTest.pass("split classification successfully");
    }

    @Test(priority = 2, dependsOnMethods = "splitClassification")
//    @AfterMethod
    public void extraction(){
        ExtentTest extentTest = extent.createTest("Content Extraction");
        extentTest.log(Status.INFO, "Extraction Started");
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
        extentTest.pass("Extraction done successfully");

    }

    @BeforeTest
    public void before(){
        extent.attachReporter(spark);
    }

    @AfterTest
    public void tearDown() {
        extent.flush();
    }

}
