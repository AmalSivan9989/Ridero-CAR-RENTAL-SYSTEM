package com.hexa.car.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.UserData;
import com.hexa.car.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(UserData user);
}