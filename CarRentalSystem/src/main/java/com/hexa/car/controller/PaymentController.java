package com.hexa.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.Payment;
import com.hexa.car.service.PaymentService;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    
    @PostMapping("/makePayment/{bookingId}")
    public ResponseEntity<?> makePayment(@PathVariable Long bookingId, @RequestBody Payment paymentDetails) {
        return paymentService.makePayment(bookingId, paymentDetails);
    }

   
    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllPayments() {
        return paymentService.getAllPayments();
    }

   
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getPaymentHistory(@PathVariable Integer userId) {
        return paymentService.getPaymentsByUserId(userId);
    }

   
    @GetMapping("/pendingPayments/{userId}")
    public ResponseEntity<?> getPendingPayments(@PathVariable Integer userId) {
        return paymentService.getPendingPayments(userId);
    }
}
