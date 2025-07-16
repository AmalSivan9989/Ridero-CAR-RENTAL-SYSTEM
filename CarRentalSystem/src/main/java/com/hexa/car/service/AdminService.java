package com.hexa.car.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;
import com.hexa.car.entity.Payment;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.repo.PaymentRepository;
import com.hexa.car.repo.UserDataRepository;

@Service
public class AdminService {

    @Autowired
    private UserDataRepository userRepo;

    @Autowired
    private CarRepository carRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    public ResponseEntity<List<UserData>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    public ResponseEntity<UserData> getUserById(Integer id) {
        UserData user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<String> deactivateUser(Integer id) {
        UserData user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setActive(false);
        userRepo.save(user);
        return ResponseEntity.ok("User with ID " + id + " deactivated.");
    }

    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepo.findAll());
    }

    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carRepo.findAll());
    }

   

public ResponseEntity<Map<String, Object>> getSummary() {
    Map<String, Object> summary = new HashMap<>();

    summary.put("totalUsers", userRepo.count());
    summary.put("users", userRepo.findAll());

    summary.put("totalCars", carRepo.count());
    summary.put("cars", carRepo.findAll());

    summary.put("availableCarsCount", carRepo.countByStatus(CarStatus.AVAILABLE));
    summary.put("availableCars", carRepo.findByStatus(CarStatus.AVAILABLE));

    summary.put("carsOnTripCount", carRepo.countByStatus(CarStatus.ON_TRIP));
    summary.put("carsOnTrip", carRepo.findByStatus(CarStatus.ON_TRIP));

    summary.put("totalBookings", bookingRepo.count());
    summary.put("bookings", bookingRepo.findAll());

    double totalRevenue = paymentRepo.findAll().stream()
        .mapToDouble(Payment::getAmount)
        .sum();
    summary.put("totalRevenue", totalRevenue);
    summary.put("payments", paymentRepo.findAll());

    return ResponseEntity.ok(summary);
}

}
