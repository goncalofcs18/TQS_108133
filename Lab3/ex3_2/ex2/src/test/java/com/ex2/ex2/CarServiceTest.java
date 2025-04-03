// CORRIGIDO_CarServiceTest.java
package com.ex2.ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    private CarRepository carRepository;
    private CarServiceImpl carService;

    @BeforeEach
    void setup() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarServiceImpl(carRepository);
    }

    @Test
    void testGetCarById() {
        Car car = new Car(1L, "Model X", "MakerX", "Electric", "Sedan", "USA");
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Optional<Car> found = carService.getCarById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getModel()).isEqualTo("Model X");
    }

    @Test
    void testSaveCar() {
        Car car = new Car(null, "Model Z", "MakerZ", "Hybrid", "SUV", "Japan");
        when(carRepository.save(car)).thenReturn(new Car(1L, "Model Z", "MakerZ", "Hybrid", "SUV", "Japan"));

        Car saved = carService.saveCar(car);
        assertThat(saved.getId()).isEqualTo(1L);
    }
}
