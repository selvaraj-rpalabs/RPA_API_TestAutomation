import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BlueXBooking extends Base{

    Path file = Path.of("./src/test/testFile/book.json");
    String applicationNo = "";
    private static HashMap hashMap = new HashMap();

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI="http://carrierbookingqa-env.eba-v2rht3ga.ap-south-1.elasticbeanstalk.com";
        RestAssured.basePath="/bookings";
    }

    @Test(priority = 1)
    public void getBookingDetail() throws Exception{
        ExtentTest extentTest = extent.createTest("BlueX Booking");
        extentTest.log(Status.INFO, "Post Booking Started");
        applicationNo =
        given()
                .contentType(ContentType.JSON)
                .body(Files.readString(file))
                .log().all()
                .when()
                .post()
                .then()
                .statusCode(201)
                .and()
                .body("status",equalTo("success"))
                .log().all()
                .extract().response()
                .path("data.booking_details.application_number").toString();
        System.out.println("Application Number is " + applicationNo);
        extentTest.pass("Booking placed successfully");
    }

//    @Test(priority = 2, dependsOnMethods = "getBookingDetail")
    public void deleteBooking(){
        hashMap.put("application_number",applicationNo);
        hashMap.put("reason","qa testing");
        hashMap.put("customer_id","test1@test.com");
        given()
                .contentType(ContentType.JSON)
                .body(hashMap)
                .log().all()
                .when()
                .delete()
                .then()
                .statusCode(201)
                .log().all()
                .body("status",equalTo("success"))
                .body("message",equalTo("Application " + applicationNo + " has been cancelled."));

    }

    @Test(priority = 3)
    public void getQuote() throws  Exception{
        ExtentTest extentTest = extent.createTest("BlueX Booking");
        extentTest.log(Status.INFO, "Get Booking Started");
        RestAssured.basePath="/bluex/quote";
        given()
                .contentType(ContentType.JSON)
                .body(Files.readString(Path.of("./src/test/testFile/quote.json")))
                .log().all()
                .when()
                .post()
                .then()
                .statusCode(201)
                .and()
                .body("status",equalTo("success"))
                .log().all();
        extentTest.pass("Get Quote done successfully");
    }

    @BeforeTest
    public void attachReport(){
        extent.attachReporter(spark);
    }

    @AfterTest
    public void flushReport() {
        extent.flush();
    }

}
