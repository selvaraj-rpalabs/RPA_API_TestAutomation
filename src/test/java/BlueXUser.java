
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.*;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BlueXUser extends  Base{

    private static String userID;

    private static String email;

    private static HashMap hashMap = new HashMap();

    @BeforeClass
    public void setUp() throws Exception{
        RestAssured.baseURI = "http://carrierbookingqa-env.eba-v2rht3ga.ap-south-1.elasticbeanstalk.com";
        RestAssured.basePath = "/bluex-user";
        email = RandomUtils.getEmail();
        hashMap = RandomUtils.getBody();
        hashMap.put("customer_id",email);

    }

    @Test(priority = 1)
    public void createUser(){
        ExtentTest extentTest = extent.createTest("Create BlueX User");
        extentTest.log(Status.INFO, "Create User Started");

        userID =
                given()
                        .contentType(ContentType.JSON)
                        .body(hashMap)
                        .log().all()
                .when()
                .post()
                .then()
                .statusCode(201)
                .and()
                .body("status",equalTo("success"))
                .log().all()
                .extract().response()
                        .path("data.user._id.$oid").toString();
        extentTest.pass("Create user done successfully");

    }

    @Test(priority = 2, dependsOnMethods = "createUser")
    public void getUser(){
        ExtentTest extentTest = extent.createTest("Get BlueX User");
        extentTest.log(Status.INFO, "Get User Started");
        given()
                .when()
                .get("/" + userID)
                .then()
                .statusCode(200)
                .and()
                .log().all()
                .header("Content-Type","application/json")
                .assertThat().body("status",equalTo("success"))
                .body("data.user._id.$oid",equalTo(userID));
        extentTest.pass("Get user done successfully");
    }

    @Test(priority = 3, dependsOnMethods = "getUser")
    public void updateUser(){
        ExtentTest extentTest = extent.createTest("Update BlueX User");
        extentTest.log(Status.INFO, "Update User Started");
        hashMap = RandomUtils.getBody();
        given()
                .contentType(ContentType.JSON)
                .body(hashMap)
                .log().all()
                .when()
                .patch("/" + userID)
                .then()
                .statusCode(200)
                .and()
                .log().all()
                .header("Content-Type","application/json")
                .assertThat().body("status",equalTo("success"))
                .body("data.user._id.$oid",equalTo(userID));
        extentTest.pass("Update user done successfully");
    }

    @Test(priority = 4, dependsOnMethods = "updateUser")
    public void deleteUser(){
        ExtentTest extentTest = extent.createTest("Delete BlueX User");
        extentTest.log(Status.INFO, "Delete User Started");
        given()
                .when()
                .delete("/" + userID)
                .then()
                .statusCode(201)
                .log().all()
                .header("Content-Type","application/json")
                .assertThat().body("status",equalTo("success"))
                .and()
                .assertThat().body("data.message",equalTo("user removed"));
        extentTest.pass("User has been deleted successfully");

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


