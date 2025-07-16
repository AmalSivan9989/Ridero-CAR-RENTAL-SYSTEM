package com.hexa.car.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.UserData;
import com.hexa.car.entity.Wallet;
import com.hexa.car.exception.InsufficientBalanceException;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.UserDataRepository;
import com.hexa.car.repo.WalletRepository;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private UserDataRepository userRepo;

    public ResponseEntity<Wallet> createWallet(Integer userId) {
        UserData user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (walletRepo.findByUser(user).isPresent()) {
            throw new RuntimeException("Wallet already exists for this user.");
        }

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(0.0)
                .build();

        return ResponseEntity.ok(walletRepo.save(wallet));
    }

    public Wallet getWalletByUserId(Integer userId) {
        UserData user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return walletRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));
    }

   public Wallet addFunds(Integer userId, double amount) {
    Wallet wallet = getWalletByUserId(userId);
    wallet.setBalance(wallet.getBalance() + amount);
    return walletRepo.save(wallet);  
}


    public ResponseEntity<String> deductFunds(Integer userId, double amount) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance. Your wallet has only ₹" + wallet.getBalance());
        }
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);
        return ResponseEntity.ok("₹" + amount + " deducted from wallet.");
    }
}
