package com.hexa.car.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Payment;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.repo.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private WalletService walletService;

    public ResponseEntity<String> makePayment(Long bookingId, Payment paymentDetails) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        UserData user = booking.getUser();
        if (user == null) {
            throw new ResourceNotFoundException("User not found for this booking.");
        }

        double amount = paymentDetails.getAmount();
        walletService.deductFunds(user.getUid(), amount); 

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod("WALLET");
        payment.setStatus("SUCCESS");
        payment.setPaymentTime(LocalDateTime.now());

        paymentRepo.save(payment);
        booking.setStatus("BOOKED");
        bookingRepo.save(booking);  
        return ResponseEntity.ok("Payment successful. ₹" + amount + " deducted from wallet.");
    }

    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepo.findAll());
    }

    public ResponseEntity<List<Payment>> getPaymentsByUserId(Integer userId) {
        return ResponseEntity.ok(paymentRepo.findByBooking_User_Uid(userId));
    }

   public ResponseEntity<List<Booking>> getPendingPayments(Integer userId) {
    List<Booking> pending = bookingRepo.findByUser_UidAndStatus(userId, "PENDING");
    return ResponseEntity.ok(pending);
}

}
