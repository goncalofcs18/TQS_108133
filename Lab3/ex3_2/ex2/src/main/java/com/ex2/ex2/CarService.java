package com.ex2.ex2;

import java.util.List;
import java.util.Optional;

public interface CarService {
    Optional<Car> getCarById(Long id);
    List<Car> getAllCars();
    Car saveCar(Car car);
    Optional<Car> findReplacementCar(Long carId);
}
