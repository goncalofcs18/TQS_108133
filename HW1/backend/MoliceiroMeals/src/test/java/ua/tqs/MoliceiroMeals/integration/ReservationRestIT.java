package ua.tqs.MoliceiroMeals.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import ua.tqs.MoliceiroMeals.MoliceiroMealsApplication;
import ua.tqs.MoliceiroMeals.dto.ReservationRequest;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Reservation;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.repository.MealRepository;
import ua.tqs.MoliceiroMeals.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MoliceiroMealsApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationRestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Meal testMeal;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        // Create test data
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        restaurantRepository.save(restaurant);

        testMeal = new Meal();
        testMeal.setDescription("Test Meal");
        testMeal.setDate(LocalDate.now());
        testMeal.setRestaurant(restaurant);
        mealRepository.save(testMeal);
    }

    @Test
    void whenCreateReservation_thenSuccess() {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Test User");
        request.put("studentNumber", "12345");
        request.put("mealId", testMeal.getId());

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/reservations")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("name", equalTo("Test User"))
            .body("studentNumber", equalTo("12345"))
            .body("used", equalTo(false))
            .body("mealDescription", equalTo("Test Meal"))
            .body("restaurantName", equalTo("Test Restaurant"));
    }

    @Test
    void whenGetNonExistentReservation_thenReturn404() {
        String nonExistentToken = UUID.randomUUID().toString();

        given()
        .when()
            .get("/reservations/" + nonExistentToken)
        .then()
            .statusCode(404);
    }

    @Test
    void whenCreateAndGetReservation_thenSuccess() {
        // Create a reservation
        Map<String, Object> request = new HashMap<>();
        request.put("name", "John Doe");
        request.put("studentNumber", "54321");
        request.put("mealId", testMeal.getId());

        String token = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/reservations")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

        // Get the reservation
        given()
        .when()
            .get("/reservations/" + token)
        .then()
            .statusCode(200)
            .body("token", equalTo(token))
            .body("name", equalTo("John Doe"))
            .body("studentNumber", equalTo("54321"))
            .body("used", equalTo(false));
    }

    @Test
    void whenUseReservation_thenSuccess() {
        // Create a reservation
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Jane Doe");
        request.put("studentNumber", "67890");
        request.put("mealId", testMeal.getId());

        String token = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/reservations")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

    
        given()
        .when()
            .post("/reservations/" + token + "/use")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));

        // Try to use it again
        given()
        .when()
            .post("/reservations/" + token + "/use")
        .then()
            .statusCode(200)
            .body("success", equalTo(false));
    }

    @Test
    void whenCancelReservation_thenSuccess() {

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Cancel User");
        request.put("studentNumber", "13579");
        request.put("mealId", testMeal.getId());

        String token = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/reservations")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

  
        given()
        .when()
            .delete("/reservations/" + token)
        .then()
            .statusCode(200)
            .body("success", equalTo(true));


        given()
        .when()
            .get("/reservations/" + token)
        .then()
            .statusCode(404);
    }
} 