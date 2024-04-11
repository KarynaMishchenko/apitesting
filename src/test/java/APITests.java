import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class APITests {
    String endpoint = "https://jsonplaceholder.typicode.com/users";

    @Test
    public void checkStatusCode(){
        given().
        when().
                get(endpoint).
        then().
                assertThat().statusCode(200);
    }

    @Test
    public void checkResponseHeaders(){
        given().
        when().
                get(endpoint).
        then().
                log().
                    headers().
                    assertThat().statusCode(200).
                    headers("Content-Type", equalTo("application/json; charset=utf-8")); //check that Content-Type header exist and has specific value
    }

    @Test
    public void checkResponseBody(){
        given().
        when().
                get(endpoint).
        then().
                assertThat().statusCode(200).
                body("size()", equalTo(10)). //check that there are 10 arrays
                body("id", everyItem(notNullValue())); //check that IDs have values
    }


    // the rest tests are connected to the videos, not to the home-task
    @Test
    public void getById(){
        ValidatableResponse response =
        given()
                .queryParam("id", 2)
                .when()
                .get(endpoint)
                .then();
        response.log().body();
    }
    @Test
    public void postRequest() {
        // Read the JSON data from the file
        String jsonString = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            jsonString = IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send a POST request with the JSON data and get the response
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonString)
                .when()
                .post(endpoint);

        // Log the response body
        System.out.println(response.getBody().asString());
    }

    @Test
    public void deleteRecord(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 10);
        String jsonString = jsonObject.toString();
        ValidatableResponse response = given().body(jsonString).when().delete(endpoint).then();
        response.log().all();
    }

}
