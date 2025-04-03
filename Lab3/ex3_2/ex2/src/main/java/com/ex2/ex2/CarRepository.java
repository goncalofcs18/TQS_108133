package com.ex2.ex2;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findBySegmentAndMotorType(String segment, String motorType);
}
