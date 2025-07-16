package com.hexa.car.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByLocationAndAvailableTrue(String location);
    List<Car> findByAvailableTrue();
    Optional<Car> findById(Long id);
    List<Car> findByLocationAndStatus(String location, String status);
    List<Car> findByStatus(String status);
    long countByStatus(CarStatus status); 
    List<Car> findByStatus(CarStatus status);
}