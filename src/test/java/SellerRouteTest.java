import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.database.DBConnection;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellerRoutesTest {
    DBConnection dbConnection = new DBConnection();

    @Test
    @Order(1)
    //Initially there will be no seller so get route will return empty array
    void testSellerOne() {
        // database connectivity
        try {
            dbConnection.createTables();
            dbConnection.resetProductTable();
            dbConnection.resetSellerTable();
        }catch (SQLException e){
            System.out.println(e);
        }

        // Setup base URI
        RestAssured.baseURI = "http://localhost:3500";

        // Send GET request to /sellers endpoint
        Response response = given()
                .when()
                .get("/seller")
                .then()
                .extract().response();

        // Check if response status code is 200 (OK)
        assertEquals(200, response.getStatusCode());

        // Check if response body is an empty array
        assertEquals("[]", response.getBody().asString());

    }

    @Test
    @Order(2)
    // storing seller "John" into database and checking the response status is 201.
    void testSellerTwo(){
        dbConnection.resetSellerTable();
        dbConnection.resetSellerTable();

        // Given
        String requestBody = "{\"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/seller");

        // Then
        response.then()
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("sellerName", equalTo("John"));
    }

    @Test
    @Order(3)
    // We have only one seller "John" in our database. Now checking if the return array contain this seller
    void testSellerThree(){
        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .get("http://localhost:3500/seller");

        // Then
        response.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[0].sellerName", equalTo("John"));
    }

    @Test
    @Order(4)
    // Checking if the duplication of seller return 400 status and text "Seller already exits"
    void testSellerFour(){
        // Given
        String requestBody = "{\"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/seller");
        System.out.println(response.body().asString());
        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Seller already exists"));
    }

    @Test
    @Order(5)
    // Checking if we don't pass sellerName, we get 400 and text "Seller name is required"
    void testSellerFive(){
        // Given
        String requestBody = "{}"; // Missing "sellerName" field

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/seller");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Seller name is required"));
    }
}

