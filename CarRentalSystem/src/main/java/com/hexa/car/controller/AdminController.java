package com.hexa.car.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Car;
import com.hexa.car.entity.UserData;
import com.hexa.car.service.AdminService;
import com.hexa.car.service.BookingService;
import com.hexa.car.service.CarService;
import com.hexa.car.service.PaymentService;
import com.hexa.car.service.UserDataService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

  

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private AdminService service;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserData>> getAllUsers() {
        return ResponseEntity.ok(userDataService.getAllUsers());
    }

    
    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserData> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userDataService.getUserById(id));
    }

 
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable Integer id, @RequestBody UserData user) {
        return ResponseEntity.ok(userDataService.updateUser(id, user));
    }

  
    @PutMapping("/deactivateUser/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userDataService.deactivateUser(id));
    }

    
    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/createBooking")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking bookingRequest) {
    return bookingService.createBooking(bookingRequest); // No wrapping needed
    }



    @GetMapping("/getAllCars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    
    @DeleteMapping("/deleteCar/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {
        carService.deleteCarById(carId);
        return ResponseEntity.ok("Car with ID " + carId + " deleted successfully.");
    }

    
    @GetMapping("/reports/summary")
public ResponseEntity<?> getReportSummary() {
    return service.getSummary();  
}



    

    @GetMapping("/getByName/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDetails getUserByUsername(@PathVariable String username) {
        return userDataService.loadUserByUsername(username);
    }
}
