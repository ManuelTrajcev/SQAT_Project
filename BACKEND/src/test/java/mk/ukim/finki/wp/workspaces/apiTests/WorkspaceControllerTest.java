package mk.ukim.finki.wp.workspaces.apiTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class WorkspaceControllerTest {

    @BeforeAll
    public static void setup() {
        // Base URI for all requests
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    // Test for Get all workspaces
    @Test
    public void testFindAll_Success() {
        RestAssured.given()
                .when()
                .get("/workspace")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("data.size()", greaterThan(0));  // Checks that we get a list of workspaces
    }

    // Test for Get my workspaces
    @Test
    public void testFindMyWorkspaces_Success() {
        Long workspaceId = 4L;

        String loginUserJson = "{\n" +
                "    \"username\": \"mt\",\n" +
                "    \"password\": \"mt\"\n" +
                "}";

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserJson)
                .when()
                .post("/user/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()))
                .extract()
                .path("token");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/workspace/my-workspaces")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("size()", greaterThan(0));  // Checks that we get the list of the user's workspaces
    }

    // Test for Accessing a specific workspace
    @Test
    public void testAccessWorkspace_Success() {
        Long workspaceId = 4L;

        // Login credentials
        String loginUserJson = "{\n" +
                "    \"username\": \"mt\",\n" +
                "    \"password\": \"mt\"\n" +
                "}";

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserJson)
                .when()
                .post("/user/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()))
                .extract()
                .path("token");

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/workspace/" + workspaceId)
                .then()
                .statusCode(200)  // Ensure HTTP 200 OK
                .body("id", equalTo(workspaceId.intValue()));
    }

    // Test for Accessing a workspace that doesn't exist
    @Test
    public void testAccessWorkspace_NotAutorized() {
        Long workspaceId = 4L;

        RestAssured.given()
                .when()
                .get("/workspace/" + workspaceId)
                .then()
                .statusCode(401);  // HTTP 401 Unautorized
    }

    // Test for Editing a workspace
    @Test
    public void testEditWorkspace_Success() {
        Long workspaceId = 4L;

        // Login credentials
        String loginUserJson = "{\n" +
                "    \"username\": \"mt\",\n" +
                "    \"password\": \"mt\"\n" +
                "}";

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserJson)
                .when()
                .post("/user/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()))
                .extract()
                .path("token");

        String editWorkspaceJson = "{\n" +
                "    \"name\": \"Updated Workspace Name\",\n" +
                "    \"description\": \"Updated Description\"\n" +
                "}";

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(editWorkspaceJson)
                .when()
                .post("/workspace/edit/" + workspaceId)
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("name", equalTo("Updated Workspace Name"))
                .body("description", equalTo("Updated Description"));
    }

    // Test for Editing a workspace that doesn't exist
    @Test
    public void testEditWorkspace_NotFound() {
        Long workspaceId = 4L;

        String editWorkspaceJson = "{\n" +
                "    \"name\": \"Updated Workspace Name\",\n" +
                "    \"description\": \"Updated Description\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(editWorkspaceJson)
                .when()
                .post("/workspace/edit/" + workspaceId)
                .then()
                .statusCode(401);  // HTTP 401 UnAuthorized
    }
}
