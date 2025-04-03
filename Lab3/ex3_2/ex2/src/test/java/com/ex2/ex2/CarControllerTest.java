// CORRIGIDO_CarControllerTest.java
package com.ex2.ex2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private Car car1, car2;

    @BeforeEach
    void setup() {
        car1 = new Car(1L, "Toyota Corolla", "Toyota", "Gasoline", "Compact", "Japan");
        car2 = new Car(2L, "Renault Clio", "Renault", "Diesel", "Compact", "France");
    }

    @Test
    void testGetAllCars() throws Exception {
        when(carService.getAllCars()).thenReturn(List.of(car1, car2));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetCarById() throws Exception {
        when(carService.getCarById(1L)).thenReturn(java.util.Optional.of(car1));

        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Toyota Corolla"));
    }
}
