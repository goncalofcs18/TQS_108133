
package com.ex2.ex2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Optional<Car> findReplacementCar(Long carId) {
        Optional<Car> original = carRepository.findById(carId);
        if (original.isEmpty()) return Optional.empty();

        List<Car> candidates = carRepository.findBySegmentAndMotorType(
                original.get().getSegment(),
                original.get().getMotorType()
        );

        return candidates.stream()
                .filter(c -> !c.getId().equals(carId))
                .findFirst();
    }

    public Optional<Car> getCarDetails(Long id) {
        return carRepository.findById(id);
    }
}
