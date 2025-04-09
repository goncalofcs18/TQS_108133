package ua.tqs.MoliceiroMeals.bdd.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ua.tqs.MoliceiroMeals.MoliceiroMealsApplication;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.repository.MealRepository;
import ua.tqs.MoliceiroMeals.repository.RestaurantRepository;
import ua.tqs.MoliceiroMeals.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MoliceiroMealsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationSteps {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MealRepository mealRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    private ResultActions resultActions;
    private MvcResult mvcResult;
    private Restaurant selectedRestaurant;
    private Meal selectedMeal;
    private String nameInput;
    private String studentNumberInput;
    private UUID reservationToken;
    
    @Before
    public void setup() {
       
        reservationRepository.deleteAll();
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
        
      
        selectedRestaurant = new Restaurant();
        selectedRestaurant.setName("Cantina de Santeodoro");
        selectedRestaurant.setLocation("Campus de Santeodoro");
        restaurantRepository.save(selectedRestaurant);
        
        selectedMeal = new Meal();
        selectedMeal.setDescription("Arroz de Pato");
        selectedMeal.setDate(LocalDate.now());
        selectedMeal.setRestaurant(selectedRestaurant);
        mealRepository.save(selectedMeal);
    }
    
    @Given("I am on the main page")
    public void i_am_on_the_main_page() {

    }
    
    @When("I select restaurant {string}")
    public void i_select_restaurant(String restaurantName) throws Exception {

        resultActions = mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk());
        
        mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        
    
        assertThat(content).contains(restaurantName);
    }
    
    @When("I select a meal from the available options")
    public void i_select_a_meal_from_the_available_options() throws Exception {

        resultActions = mockMvc.perform(get("/api/meals/restaurant/" + selectedRestaurant.getId()))
                .andExpect(status().isOk());
                
        mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        
    
        assertThat(content).contains(selectedMeal.getDescription());
    }
    
    @When("I enter my name {string} and student number {string}")
    public void i_enter_my_name_and_student_number(String name, String studentNumber) {
       
        nameInput = name;
        studentNumberInput = studentNumber;
    }
    
    @When("I submit the reservation")
    public void i_submit_the_reservation() throws Exception {
    
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", nameInput);
        requestBody.put("studentNumber", studentNumberInput);
        requestBody.put("mealId", selectedMeal.getId());
        
     
        resultActions = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameInput))
                .andExpect(jsonPath("$.studentNumber").value(studentNumberInput))
                .andExpect(jsonPath("$.token").isNotEmpty());
                
        mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        
       
        reservationToken = UUID.fromString(response.get("token").toString());
    }
    
    @Then("I should see a confirmation with a reservation code")
    public void i_should_see_a_confirmation_with_a_reservation_code() {
 
        assertThat(reservationToken).isNotNull();
    }
    
    @Then("the reservation should be saved in the system")
    public void the_reservation_should_be_saved_in_the_system() throws Exception {
        
        mockMvc.perform(get("/api/reservations/" + reservationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameInput))
                .andExpect(jsonPath("$.studentNumber").value(studentNumberInput))
                .andExpect(jsonPath("$.token").value(reservationToken.toString()));
    }
    
    @Given("I have a used reservation with token {string}")
    public void i_have_a_used_reservation_with_token(String tokenStr) throws Exception {
        
        UUID token = UUID.fromString(tokenStr);
        
       
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Test User");
        requestBody.put("studentNumber", "12345");
        requestBody.put("mealId", selectedMeal.getId());
        
        resultActions = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
                
        mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        
      
        reservationToken = UUID.fromString(response.get("token").toString());
        
   
        mockMvc.perform(post("/api/reservations/" + reservationToken + "/use"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @When("I try to use the reservation")
    public void i_try_to_use_the_reservation() throws Exception {
       
        resultActions = mockMvc.perform(post("/api/reservations/" + reservationToken + "/use"))
                .andExpect(status().isOk());
    }
    
    @Then("I should see a message that the reservation is already used")
    public void i_should_see_a_message_that_the_reservation_is_already_used() throws Exception {
        
        resultActions.andExpect(jsonPath("$.success").value(false));
    }
} 