import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.database.DBConnection;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class ProductRouteTest {
    DBConnection dbConnection = new DBConnection();
    ProductRouteTest(){
        try{
            dbConnection.createTables();
            dbConnection.resetProductTable();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    @Test
    @Order(1)
    // Initially there will be no product so we are going to get empty array
    void testProductOne(){


        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .get("http://localhost:3500/product");
        // Then
        response.then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", empty());
    }

    @Test
    @Order(2)
        // Verifying non-existing seller
    void testProductTwo() {
        // Given
        String requestBody = "{\"productId\":\"12345\", \"productName\":\"Hair Oil\", \"productPrice\":10, \"sellerName\":\"NonExistingSeller\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Seller does not exist"));
    }

    @Test
    @Order(3)
    // storing product in a database
    void testProductThree(){
        // Given
        String requestBody = "{\"productId\":\"1234\", \"productName\":\"body wash\", \"productPrice\":10, \"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");
        System.out.println(response.body().asString());
        // Then
        response.then()
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("productId", equalTo("1234"))
                .body("productName", equalTo("body wash"))
                .body("productPrice", equalTo(10f))
                .body("seller.sellerName", equalTo("John"));

    }

    @Test
    @Order(4)
        // Verifying price less than or equal to 0
    void testProductFour() {
        // Given
        String requestBody = "{\"productId\":\"12345\", \"productName\":\"Laptop\", \"productPrice\":0, \"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Product price must be greater than 0"));
    }

    @Test
    @Order(5)
    // Verifying missing id
    void testProductFive() {
        // Given
        String requestBody = "{ \"productName\":\"Laptop\", \"productPrice\":0, \"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Product id is required"));
    }

    @Test
    @Order(6)
        // Verifying missing product name
    void testProductSix() {
        // Given
        String requestBody = "{ \"productId\":\"32456\", \"productPrice\":0, \"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Product name is required"));
    }

    @Test
    @Order(7)
        // Verifying missing product price
    void testProductSeven() {
        // Given
        String requestBody = "{ \"productId\":\"32456\", \"productName\":\"Iphone\", \"sellerName\":\"John\"}";

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("http://localhost:3500/product");

        // Then
        response.then()
                .assertThat()
                .statusCode(400)
                .body(equalTo("Product price is required"));
    }




    @Test
    @Order(8)
    // Get a single product with id which is not present in database. we will get 404 and text "Product not found."
    void testProductEight(){
        // Given
        String productId = "1234897"; // Assuming productId for the test

        // When
        Response response = given()
                .contentType(ContentType.JSON)
                .get("http://localhost:3500/product/" + productId);

        // Then
        response.then()
                .assertThat()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body(equalTo("Product not found."));
    }


    @Test
    @Order(9)
    // always get status 200
    void testProductNine(){
        String id = "1234"; // Assuming productId for the test

        // When
        Response response = given()
                .delete("http://localhost:3500/product/" + id);
        System.out.println(response.body().asString());
        // Then
        response.then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("Deleted product successfully"));
    }
}
