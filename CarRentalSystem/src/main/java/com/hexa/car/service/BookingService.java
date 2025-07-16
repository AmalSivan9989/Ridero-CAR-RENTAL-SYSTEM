package com.hexa.car.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.repo.CarRepository;
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;
     @Autowired
    private CarRepository carRepo;

    public ResponseEntity<Booking> createBooking(Booking booking) {

    if (booking.getCheckInTime() == null || booking.getCheckOutTime() == null) {
        return ResponseEntity.badRequest().body(null);
    }

    booking.setStatus("PENDING");

    Car car = booking.getCar();
    if (car != null) {
        car.setStatus(CarStatus.ON_TRIP);
        booking.setCar(car);
    } else {
        return ResponseEntity.badRequest().body(null);
    }

    Duration duration = Duration.between(booking.getCheckInTime(), booking.getCheckOutTime());
    long days = Math.max(1, duration.toDays());
    double totalAmount = booking.getCar().getPricePerDay() * days;
    booking.setTotalAmount(totalAmount);

    Booking saved = bookingRepo.save(booking);
    return ResponseEntity.ok(saved);
}



    public ResponseEntity<List<Booking>> getBookingsByUser(UserData user) {
        return ResponseEntity.ok(bookingRepo.findByUser(user));
    }

    public ResponseEntity<String> cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + bookingId + " not found."));
        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);
        return ResponseEntity.ok("Booking with ID " + bookingId + " cancelled successfully.");
    }

 
public List<Booking> getAllBookings() {
    return bookingRepo.findAll();
}


    public ResponseEntity<Booking> updateBooking(Long id, Booking newBooking) {
        Booking existing = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        existing.setPickupDate(newBooking.getPickupDate());
        existing.setDropoffDate(newBooking.getDropoffDate());
        existing.setPickupLocation(newBooking.getPickupLocation());
        existing.setDropoffLocation(newBooking.getDropoffLocation());

        Booking updated = bookingRepo.save(existing);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<String> checkOut(Long bookingId) {
    Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    
    booking.setCheckOutTime(LocalDateTime.now());
    booking.setStatus("COMPLETED"); 
    
    Car car = booking.getCar();
    if (car != null) {
        car.setStatus(CarStatus.AVAILABLE); 
        car.setAvailable(true);           
        carRepo.save(car);                
    }
    
    bookingRepo.save(booking);
    return ResponseEntity.ok("Check-out completed and car marked AVAILABLE.");
}


    public ResponseEntity<String> checkIn(Long bookingId) {
    Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

    booking.setCheckInTime(LocalDateTime.now());
    booking.setStatus("CHECKED_IN"); 

    Car car = booking.getCar();
    car.setStatus(CarStatus.UNAVAILABLE); 
    car.setAvailable(false);

    carRepo.save(car);
    bookingRepo.save(booking);

    return ResponseEntity.ok("Check-in completed and car marked ON_TRIP.");
}


}
