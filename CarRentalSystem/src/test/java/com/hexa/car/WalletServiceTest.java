package com.hexa.car;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import com.hexa.car.entity.UserData;
import com.hexa.car.entity.Wallet;
import com.hexa.car.exception.InsufficientBalanceException;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.UserDataRepository;
import com.hexa.car.repo.WalletRepository;
import com.hexa.car.service.WalletService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepo;

    @Mock
    private UserDataRepository userRepo;

    private UserData user;
    private Wallet wallet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new UserData();
        user.setUid(1);
        user.setName("TestUser");

        wallet = Wallet.builder()
                .id((long) 1)
                .user(user)
                .balance(1000.0)
                .build();
    }

    @Test
    public void testCreateWallet_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.empty());
        when(walletRepo.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Wallet> response = walletService.createWallet(1);

        assertNotNull(response.getBody());
        assertEquals(user, response.getBody().getUser());
        assertEquals(0.0, response.getBody().getBalance(), 0.01);

        verify(userRepo).findById(1);
        verify(walletRepo).findByUser(user);
        verify(walletRepo).save(any(Wallet.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreateWallet_UserNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        walletService.createWallet(1);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWallet_AlreadyExists() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));

        walletService.createWallet(1);
    }

    @Test
    public void testGetWalletByUserId_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletByUserId(1);

        assertEquals(wallet, result);
        verify(userRepo).findById(1);
        verify(walletRepo).findByUser(user);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetWalletByUserId_UserNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        walletService.getWalletByUserId(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetWalletByUserId_WalletNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.empty());

        walletService.getWalletByUserId(1);
    }

    @Test
    public void testAddFunds() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));
        when(walletRepo.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        Wallet updated = walletService.addFunds(1, 500.0);

        assertEquals(1500.0, updated.getBalance(), 0.01);
        verify(walletRepo).save(wallet);
    }

    @Test
    public void testDeductFunds_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));
        when(walletRepo.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = walletService.deductFunds(1, 500.0);

        assertEquals("₹500.0 deducted from wallet.", response.getBody());
        assertEquals(500.0, wallet.getBalance(), 0.01);

        verify(walletRepo).save(wallet);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void testDeductFunds_InsufficientBalance() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));

        walletService.deductFunds(1, 2000.0);
    }
}
