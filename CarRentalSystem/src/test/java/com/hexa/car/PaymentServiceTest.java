package com.hexa.car;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Payment;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.repo.PaymentRepository;
import com.hexa.car.service.PaymentService;
import com.hexa.car.service.WalletService;

public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private WalletService walletService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
public void testMakePayment_Success() {
    Long bookingId = 1L;
    UserData user = new UserData();
    user.setUid(10);

    Booking booking = new Booking();
    booking.setId(bookingId);
    booking.setUser(user);
    booking.setStatus("PENDING");

    Payment paymentDetails = new Payment();
    paymentDetails.setAmount(1000.0);

    when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
    when(walletService.deductFunds(user.getUid(), 1000.0))
        .thenReturn(ResponseEntity.ok("Funds deducted"));
    when(paymentRepo.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
    when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);


    ResponseEntity<String> response = paymentService.makePayment(bookingId, paymentDetails);

    assertTrue(response.getBody().contains("Payment successful"));
    assertEquals("BOOKED", booking.getStatus());

    verify(walletService).deductFunds(user.getUid(), 1000.0);
    verify(paymentRepo).save(any(Payment.class));
    verify(bookingRepo).save(booking);
}


    @Test(expected = ResourceNotFoundException.class)
    public void testMakePayment_BookingNotFound() {
        Long bookingId = 1L;
        Payment paymentDetails = new Payment();

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.empty());
        paymentService.makePayment(bookingId, paymentDetails);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testMakePayment_UserNotFound() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setUser(null);

        Payment paymentDetails = new Payment();

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        paymentService.makePayment(bookingId, paymentDetails);
    }

    @Test
    public void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentRepo.findAll()).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentService.getAllPayments();

        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetPaymentsByUserId() {
        Integer userId = 10;
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentRepo.findByBooking_User_Uid(userId)).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentService.getPaymentsByUserId(userId);

        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetPendingPayments() {
        Integer userId = 10;
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepo.findByUser_UidAndStatus(userId, "PENDING")).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = paymentService.getPendingPayments(userId);

        assertEquals(2, response.getBody().size());
    }
}
