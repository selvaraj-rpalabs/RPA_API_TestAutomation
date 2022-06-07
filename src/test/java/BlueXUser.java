import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BlueXUser {

    private static String userID;

    private static String email;

    private static HashMap hashMap = new HashMap();

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "http://carrierbookingqa-env.eba-v2rht3ga.ap-south-1.elasticbeanstalk.com";
        RestAssured.basePath = "/bluex-user";
        email = RandomUtils.getEmail();
        hashMap = RandomUtils.getBody();
        hashMap.put("customer_id",email);
    }

    @Test(priority = 1)
    public void createUser(){
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

    }

    @Test(priority = 2, dependsOnMethods = "createUser")
    public void getUser(){
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
    }

    @Test(priority = 3, dependsOnMethods = "getUser")
    public void updateUser(){
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
    }

    @Test(priority = 4, dependsOnMethods = "updateUser")
    public void deleteUser(){
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
    }
}


