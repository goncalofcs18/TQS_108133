package ua.tqs.MoliceiroMeals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs.MoliceiroMeals.dto.ReservationDTO;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Reservation;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.service.ReservationService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void whenCreateReservation_thenReturnReservation() throws Exception {
        // Arrange
        ReservationDTO dto = new ReservationDTO();
        dto.setName("John Doe");
        dto.setStudentNumber("100000");
        dto.setMealId(1L);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setDescription("Test Meal");
        meal.setDate(LocalDate.now());
        meal.setRestaurant(restaurant);
        
        Reservation reservation = new Reservation("John Doe", "100000", meal);
        UUID token = UUID.randomUUID();
        reservation.setToken(token);
        
        when(reservationService.createReservation(anyString(), anyString(), anyLong()))
                .thenReturn(reservation);
        
        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token.toString())))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.studentNumber", is("100000")))
                .andExpect(jsonPath("$.used", is(false)));
    }
    
    @Test
    public void whenGetInvalidReservation_thenReturn404() throws Exception {
        // Arrange
        String invalidToken = UUID.randomUUID().toString();
        when(reservationService.getReservationByToken(any(UUID.class)))
                .thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/reservations/" + invalidToken))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void whenGetValidReservation_thenReturnReservation() throws Exception {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        reservation.setName("John Doe");
        reservation.setStudentNumber("100000");
        reservation.setUsed(false);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setDescription("Test Meal");
        meal.setDate(LocalDate.now());
        meal.setRestaurant(restaurant);
        
        reservation.setMeal(meal);
        
        when(reservationService.getReservationByToken(token))
                .thenReturn(Optional.of(reservation));
        
        // Act & Assert
        mockMvc.perform(get("/api/reservations/" + token.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token.toString())))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.studentNumber", is("100000")))
                .andExpect(jsonPath("$.used", is(false)));
    }
    
    @Test
    public void whenUseValidReservation_thenReturnSuccess() throws Exception {
        // Arrange
        UUID token = UUID.randomUUID();
        when(reservationService.useReservation(token)).thenReturn(true);
        
        // Act & Assert
        mockMvc.perform(post("/api/reservations/" + token.toString() + "/use"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
    
    @Test
    public void whenUseInvalidReservation_thenReturnFailure() throws Exception {
        // Arrange
        UUID token = UUID.randomUUID();
        when(reservationService.useReservation(token)).thenReturn(false);
        
        // Act & Assert
        mockMvc.perform(post("/api/reservations/" + token.toString() + "/use"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }
    
    @Test
    public void whenCancelValidReservation_thenReturnSuccess() throws Exception {
        // Arrange
        UUID token = UUID.randomUUID();
        when(reservationService.cancelReservation(token)).thenReturn(true);
        
        // Act & Assert
        mockMvc.perform(delete("/api/reservations/" + token.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
    
    @Test
    public void whenCancelInvalidReservation_thenReturnFailure() throws Exception {
        // Arrange
        UUID token = UUID.randomUUID();
        when(reservationService.cancelReservation(token)).thenReturn(false);
        
        // Act & Assert
        mockMvc.perform(delete("/api/reservations/" + token.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }
} 