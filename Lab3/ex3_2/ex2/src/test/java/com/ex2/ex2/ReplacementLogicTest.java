// src/test/java/com/ex2/ex2/ReplacementLogicTest.java
package com.ex2.ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReplacementLogicTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();

        carRepository.saveAll(List.of(
                new Car(null, "Toyota Corolla", "Gasoline", "Compact", "Japan", "Blue"),
                new Car(null, "Renault Clio", "Gasoline", "Compact", "France", "White")
        ));
    }

    @Test
    void testFindReplacementCar() {
        Car original = carRepository.findAll().get(0);
        Optional<Car> replacement = carService.findReplacementCar(original.getId());

        assertThat(replacement).isPresent();
        assertThat(replacement.get().getId()).isNotEqualTo(original.getId());
        assertThat(replacement.get().getSegment()).isEqualTo(original.getSegment());
        assertThat(replacement.get().getMotorType()).isEqualTo(original.getMotorType());
    }
}
