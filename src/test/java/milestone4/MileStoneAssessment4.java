package milestone4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MileStoneAssessment4 {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";

        // Request Specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL) 
                .build();

      
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void testGetUser() {
        given()
                .spec(requestSpec)
                .when()
                .get("/api/users/2")
                .then()
                .spec(responseSpec)
                .assertThat()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue())
                .body("data.first_name", equalTo("Janet"));
    }

    @Test
    public void testCreateUser() {
        String requestBody = "{ \"name\": \"testingUser\", \"job\": \"Tester\" }";

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .spec(responseSpec)
                .assertThat()
                .statusCode(201)
                .body("name", equalTo("testingUser"))
                .body("job", equalTo("Tester"))
                .extract()
                .response();

        String id = response.jsonPath().getString("id");
        assert id != null;
    }

    @Test
    public void testUpdateUserPut() {
        String requestBody = "{ \"name\": \"UpdateTest\", \"job\": \"Update\" }";

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put("/api/users/2")
                .then()
                .spec(responseSpec)
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("UpdateTest"))
                .body("job", equalTo("Update"));
    }

    @Test
    public void testUpdateUserPatch() {
        String requestBody = "{ \"job\": \"Test Engineer\" }";

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .patch("/api/users/2")
                .then()
                .spec(responseSpec)
                .assertThat()
                .statusCode(200)
                .body("job", equalTo("Test Engineer"));
    }

    @Test
    public void testDeleteUser() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/api/users/2")
                .then()                 
                .statusCode(204); 
    }
}
