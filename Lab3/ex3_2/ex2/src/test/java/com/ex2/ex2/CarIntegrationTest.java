package com.ex2.ex2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
    }

    @Test
    void whenGetAllCars_thenStatus200AndReturnCars() throws Exception {
        carRepository.saveAll(List.of(
                new Car("Toyota Corolla", "Toyota", "Gasoline", "Compact", "Japan"),
                new Car("Renault Clio", "Renault", "Diesel", "Compact", "France")
        ));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void whenGetCarById_thenStatus200AndReturnCar() throws Exception {
        Car car = new Car("Tesla Model 3", "Tesla", "Electric", "Sedan", "USA");
        car = carRepository.save(car);

        mockMvc.perform(get("/api/cars/" + car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Tesla Model 3"))
                .andExpect(jsonPath("$.maker").value("Tesla"))
                .andExpect(jsonPath("$.type").value("Electric"))
                .andExpect(jsonPath("$.segment").value("Sedan"))
                .andExpect(jsonPath("$.origin").value("USA"));
    }
}
