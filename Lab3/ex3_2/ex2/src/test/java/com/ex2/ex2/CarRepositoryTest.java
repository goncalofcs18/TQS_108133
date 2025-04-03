// CORRIGIDO_CarRepositoryTest.java
package com.ex2.ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setup() {
        carRepository.deleteAll();
        carRepository.saveAll(List.of(
                new Car(null, "Model A", "MakerA", "Diesel", "SUV", "Germany"),
                new Car(null, "Model B", "MakerB", "Diesel", "SUV", "Germany"),
                new Car(null, "Model C", "MakerC", "Gasoline", "Sedan", "France")
        ));
    }

    @Test
    void testFindBySegmentAndMotorType() {
        List<Car> results = carRepository.findBySegmentAndMotorType("SUV", "Diesel");
        assertThat(results).hasSize(2);
    }
}
