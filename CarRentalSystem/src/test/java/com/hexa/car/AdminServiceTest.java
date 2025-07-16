package com.hexa.car;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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
import com.hexa.car.service.AdminService;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserDataRepository userRepo;

    @Mock
    private CarRepository carRepo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<UserData> users = Arrays.asList(
            new UserData(1, "Amal", "amal@gmail.com", "pass", "ROLE_USER", true),
            new UserData(2, "Raj", "raj@gmail.com", "pass", "ROLE_USER", true)
        );
        when(userRepo.findAll()).thenReturn(users);

        ResponseEntity<List<UserData>> response = adminService.getAllUsers();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetUserByIdFound() {
        UserData user = new UserData(1, "Amal", "amal@gmail.com", "pass", "ROLE_USER", true);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<UserData> response = adminService.getUserById(1);
        assertEquals("Amal", response.getBody().getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByIdNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        adminService.getUserById(1);
    }

    @Test
    public void testDeactivateUser() {
        UserData user = new UserData(1, "Amal", "amal@gmail.com", "pass", "ROLE_USER", true);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(user);

        ResponseEntity<String> response = adminService.deactivateUser(1);
        assertEquals("User with ID 1 deactivated.", response.getBody());
        assertFalse(user.isActive());
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = Arrays.asList(
            new Booking(), new Booking()
        );
        when(bookingRepo.findAll()).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = adminService.getAllBookings();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetAllCars() {
        List<Car> cars = Arrays.asList(
            new Car(), new Car(), new Car()
        );
        when(carRepo.findAll()).thenReturn(cars);

        ResponseEntity<List<Car>> response = adminService.getAllCars();
        assertEquals(3, response.getBody().size());
    }

    @Test
    public void testGetSummary() {
        when(userRepo.count()).thenReturn(10L);
        when(carRepo.count()).thenReturn(5L);
        when(carRepo.countByStatus(CarStatus.AVAILABLE)).thenReturn(3L);
        when(carRepo.countByStatus(CarStatus.ON_TRIP)).thenReturn(2L);
        when(bookingRepo.count()).thenReturn(20L);

        List<Payment> payments = Arrays.asList(
            Payment.builder().amount(500.0).build(),
            Payment.builder().amount(1500.0).build()
        );
        when(paymentRepo.findAll()).thenReturn(payments);

        ResponseEntity<Map<String, Object>> response = adminService.getSummary();
        Map<String, Object> summary = response.getBody();
        assertEquals(10L, summary.get("totalUsers"));
        assertEquals(5L, summary.get("totalCars"));
        assertEquals(3L, summary.get("availableCars"));
        assertEquals(2L, summary.get("carsOnTrip"));
        assertEquals(20L, summary.get("totalBookings"));
        assertEquals(2000.0, (Double) summary.get("totalRevenue"), 0.01);
    }
}
