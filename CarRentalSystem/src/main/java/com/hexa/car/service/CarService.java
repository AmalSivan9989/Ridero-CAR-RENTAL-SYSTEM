package com.hexa.car.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepo;

    public ResponseEntity<String> addCar(Car car) {
        car.setStatus(CarStatus.AVAILABLE);
        carRepo.save(car);
        return ResponseEntity.ok("Car added successfully.");
    }

    public ResponseEntity<List<Car>> getAllAvailableCars() {
        return ResponseEntity.ok(carRepo.findByAvailableTrue());
    }

    public ResponseEntity<List<Car>> getCarsByLocation(String location) {
        return ResponseEntity.ok(carRepo.findByLocationAndAvailableTrue(location));
    }

    public ResponseEntity<String> updateAvailability(Long carId, boolean available) {
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + carId + " not found."));
        car.setAvailable(available);
        carRepo.save(car);
        return ResponseEntity.ok("Car availability updated successfully.");
    }

    public List<Car> getAllCars() {
    return carRepo.findAll();
}

    public ResponseEntity<String> deleteCarById(Long carId) {
        if (carRepo.existsById(carId)) {
            carRepo.deleteById(carId);
            return ResponseEntity.ok("Car deleted successfully.");
        }
        throw new ResourceNotFoundException("Car with ID " + carId + " does not exist.");
    }

    public ResponseEntity<Car> getCarById(Long carId) {
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + carId));
        return ResponseEntity.ok(car);
    }
}
