package com.hexa.car.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.service.BookingService;
import com.hexa.car.service.CarService;
import com.hexa.car.service.UserDataService;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private CarService carService;

     @Autowired
    private BookingRepository bookingRepo;
    @PostMapping("/createBooking")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, Principal principal) {
        UserData user = userDataService.getUserDataByName(principal.getName());
       Car car = carService.getCarById(booking.getCar().getId()).getBody();


        booking.setUser(user);
        booking.setCar(car);

        return bookingService.createBooking(booking);
    }

   
    @GetMapping("/getMyBookings")
    public ResponseEntity<?> getMyBookings(Principal principal) {
        UserData user = userDataService.getUserDataByName(principal.getName());
        return bookingService.getBookingsByUser(user);
    }


    @PutMapping("/cancelBooking/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

   
    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.updateBooking(id, booking);
    }


    @PutMapping("/checkin/{bookingId}")
    public ResponseEntity<?> checkIn(@PathVariable Long bookingId) {
        return bookingService.checkIn(bookingId);
    }

  
   @PostMapping("/checkout/{bookingId}")
public ResponseEntity<String> checkOut(@PathVariable Long bookingId) {
    Booking booking = bookingRepo.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

    booking.setStatus("COMPLETED");  
    bookingRepo.save(booking);

    return ResponseEntity.ok("Checked out successfully");
}

}
