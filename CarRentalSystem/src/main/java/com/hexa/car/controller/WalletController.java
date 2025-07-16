package com.hexa.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.Wallet;
import com.hexa.car.service.WalletService;

@RestController
@RequestMapping("/wallet")
@CrossOrigin(origins = "http://localhost:4200")
public class WalletController {

    @Autowired
    private WalletService walletService;

   
    @PostMapping("/createWallet/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable Integer userId) {
        return walletService.createWallet(userId);
    }

   
    @GetMapping("/getWalletBalance/{userId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Integer userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

 
    @PutMapping("/addFunds/{userId}/{amount}")
public ResponseEntity<Wallet> addFunds(@PathVariable Integer userId, @PathVariable double amount) {
    Wallet updatedWallet = walletService.addFunds(userId, amount);
    return ResponseEntity.ok(updatedWallet);
}


   
    @PutMapping("/deductFunds/{userId}/{amount}")
    public ResponseEntity<?> deductFunds(@PathVariable Integer userId, @PathVariable double amount) {
        return walletService.deductFunds(userId, amount);
    }
}
