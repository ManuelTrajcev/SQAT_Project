package mk.ukim.finki.wp.workspaces.apiTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class UserControllerTest {

    @BeforeAll
    public static void setup() {
        // Base URI for all requests
        RestAssured.baseURI = "http://localhost:8080/api/user";
    }

    // Test for User Registration
    @Test
    public void testRegister_Success() {
        // Create a valid user registration DTO
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password123\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("username", equalTo("newuser"))
                .body("email", equalTo("newuser@example.com"));

        RestAssured.given()
                .when()
                .delete("/delete/newuser")
                .then()
                .statusCode(200) // HTTP 200 OK
                .body(equalTo("User deleted successfully"));
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        // First, register a user to ensure the username is taken
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password123\"\n" +
                "}";

        // Register the user once
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("username", equalTo("newuser"))
                .body("email", equalTo("newuser@example.com"));

        // Try to register the same user again
        String duplicateUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password123\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(duplicateUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(400);  // HTTP 400 Bad Request

        RestAssured.given()
                .when()
                .delete("/delete/newuser")
                .then()
                .statusCode(200) // HTTP 200 OK
                .body(equalTo("User deleted successfully"));
    }


    // Test for User Registration with Invalid Passwords
    @Test
    public void testRegister_Failure_InvalidPasswords() {
        // Create a user DTO where passwords don't match
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password21323\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(400);  // HTTP 400 Bad Request
    }

    // Test for User Login Success
    @Test
    public void testLogin_Success() {
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password123\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("username", equalTo("newuser"))
                .body("email", equalTo("newuser@example.com"));
        // Arrange: Define login credentials
        String loginUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // Act and Assert: Make the login request and check the response
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserJson)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()));

        RestAssured.given()
                .when()
                .delete("/delete/newuser")
                .then()
                .statusCode(200) // HTTP 200 OK
                .body(equalTo("User deleted successfully"));
    }


    // Test for User Login Failure with Invalid Credentials
    @Test
    public void testLogin_Failure_InvalidCredentials() {
        String loginUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"password\": \"wrongpassword\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserJson)
                .when()
                .post("/login")
                .then()
                .statusCode(404);  // HTTP 404 user not found
    }

    // Test for User Logout
    @Test
    public void testLogout_Success() {
        RestAssured.given()
                .when()
                .get("/logout")
                .then()
                .statusCode(200);  // HTTP 200 OK for successful logout
    }

    // Test for User Registration with Missing Fields (Bad Request)
    @Test
    public void testRegister_Failure_MissingFields() {
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\"\n" +
                "}";  // Missing password and repeatPassword fields

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(400);  // HTTP 400 Bad Request for missing fields
    }

    @Test
    public void testRegisterAndDelete_UserAlreadyExists() {
        String createUserJson = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"newuser@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"repeatPassword\": \"password123\"\n" +
                "}";

        // Register the user once
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserJson)
                .when()
                .post("/register")
                .then()
                .statusCode(200)  // HTTP 200 OK
                .body("username", equalTo("newuser"))
                .body("email", equalTo("newuser@example.com"));

        // Now, delete the created user
        RestAssured.given()
                .when()
                .delete("/delete/newuser")
                .then()
                .statusCode(200) // HTTP 200 OK
                .body(equalTo("User deleted successfully"));

        // Try to fetch the deleted user (should return 404)
        RestAssured.given()
                .when()
                .get("/{username}", "newuser")
                .then()
                .statusCode(404);  // HTTP 404 Not Found
    }

    @Test
    public void testRegisterAndDelete_UserNotFound() {
        // Try to delete a user that doesn't exist
        RestAssured.given()
                .when()
                .delete("/api/user/delete/nonexistentuser")
                .then()
                .statusCode(404); // HTTP 404 Not Found
    }

}
