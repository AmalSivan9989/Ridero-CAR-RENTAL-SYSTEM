package com.hexa.car.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.Car;
import com.hexa.car.service.CarService;

@RestController
@RequestMapping("/cars")
@CrossOrigin(origins = "http://localhost:4200")
public class CarController {

    @Autowired
    private CarService carService;

  
    @GetMapping("/getAllAvailableCars")
    public ResponseEntity<List<Car>> getAllAvailableCars() {
        return carService.getAllAvailableCars();
    }

   
    @GetMapping("/getCarsByLocation/{location}")
    public ResponseEntity<List<Car>> getCarsByLocation(@PathVariable String location) {
        return carService.getCarsByLocation(location);
    }

  
    @PostMapping("/addCar")
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    
    @PutMapping("/updateAvailability/{carId}")
    public ResponseEntity<String> updateAvailability(
            @PathVariable Long carId,
            @RequestParam boolean available) {
        return carService.updateAvailability(carId, available);
    }

   
    @GetMapping("/getAllCars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

   
    @DeleteMapping("/deleteCar/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {
        return carService.deleteCarById(carId);
    }


    @GetMapping("/getCarById/{carId}")
    public ResponseEntity<Car> getCarById(@PathVariable Long carId) {
        return carService.getCarById(carId);
    }
}
