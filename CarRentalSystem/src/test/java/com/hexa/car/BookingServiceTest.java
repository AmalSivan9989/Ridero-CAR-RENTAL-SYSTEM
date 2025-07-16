package com.hexa.car;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.BookingRepository;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.service.BookingService;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private CarRepository carRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBooking_Success() {
        Car car = new Car();
        car.setPricePerDay(100.0);
        car.setStatus(CarStatus.AVAILABLE);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setCheckInTime(LocalDateTime.now().minusDays(1));
        booking.setCheckOutTime(LocalDateTime.now());

        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Booking> response = bookingService.createBooking(booking);

        assertNotNull(response.getBody());
        assertEquals("PENDING", response.getBody().getStatus());
        assertEquals(CarStatus.ON_TRIP, booking.getCar().getStatus());
        assertTrue(response.getBody().getTotalAmount() >= 100.0);
    }

    @Test
    public void testCreateBooking_MissingCar() {
        Booking booking = new Booking();
        booking.setCheckInTime(LocalDateTime.now());
        booking.setCheckOutTime(LocalDateTime.now().plusDays(1));
        booking.setCar(null);

        ResponseEntity<Booking> response = bookingService.createBooking(booking);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateBooking_MissingTimes() {
        Booking booking = new Booking();
        booking.setCar(new Car());

        ResponseEntity<Booking> response = bookingService.createBooking(booking);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetBookingsByUser() {
        UserData user = new UserData();
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());

        when(bookingRepo.findByUser(user)).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingService.getBookingsByUser(user);
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testCancelBooking_Success() {
        Booking booking = new Booking();
        booking.setStatus("PENDING");
        booking.setId(1L);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        ResponseEntity<String> response = bookingService.cancelBooking(1L);
        assertEquals("Booking with ID 1 cancelled successfully.", response.getBody());
        assertEquals("CANCELLED", booking.getStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCancelBooking_NotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        bookingService.cancelBooking(1L);
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking(), new Booking());
        when(bookingRepo.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();
        assertEquals(3, result.size());
    }

    @Test
    public void testUpdateBooking_Success() {
        Booking existing = new Booking();
        existing.setPickupDate(LocalDateTime.now().minusDays(2));
        existing.setDropoffDate(LocalDateTime.now().minusDays(1));

        Booking newBooking = new Booking();
        newBooking.setPickupDate(LocalDateTime.now());
        newBooking.setDropoffDate(LocalDateTime.now().plusDays(1));
        newBooking.setPickupLocation("Loc1");
        newBooking.setDropoffLocation("Loc2");

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Booking> response = bookingService.updateBooking(1L, newBooking);
        assertEquals("Loc1", response.getBody().getPickupLocation());
        assertEquals("Loc2", response.getBody().getDropoffLocation());
        assertEquals(newBooking.getPickupDate(), response.getBody().getPickupDate());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateBooking_NotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        bookingService.updateBooking(1L, new Booking());
    }

    @Test
    public void testCheckOut_Success() {
        Car car = new Car();
        car.setStatus(CarStatus.ON_TRIP);
        car.setAvailable(false);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setStatus("PENDING");

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(carRepo.save(any(Car.class))).thenReturn(car);
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        ResponseEntity<String> response = bookingService.checkOut(1L);

        assertEquals("Check-out completed and car marked AVAILABLE.", response.getBody());
        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertTrue(car.isAvailable());
        assertEquals("COMPLETED", booking.getStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCheckOut_NotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        bookingService.checkOut(1L);
    }

    @Test
    public void testCheckIn_Success() {
        Car car = new Car();
        car.setStatus(CarStatus.AVAILABLE);
        car.setAvailable(true);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setStatus("PENDING");

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(carRepo.save(any(Car.class))).thenReturn(car);
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        ResponseEntity<String> response = bookingService.checkIn(1L);

        assertEquals("Check-in completed and car marked ON_TRIP.", response.getBody());
        assertEquals(CarStatus.ON_TRIP, car.getStatus());
        assertFalse(car.isAvailable());
        assertEquals("CHECKED_IN", booking.getStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCheckIn_NotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        bookingService.checkIn(1L);
    }
}

