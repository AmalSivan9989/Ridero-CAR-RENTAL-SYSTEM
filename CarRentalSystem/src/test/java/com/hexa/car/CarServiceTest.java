package com.hexa.car;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.CarStatus;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.service.CarService;

public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCar() {
        Car car = new Car();
        car.setAvailable(true);
        car.setStatus(CarStatus.AVAILABLE);

        when(carRepo.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = carService.addCar(car);
        assertEquals("Car added successfully.", response.getBody());
        assertEquals(CarStatus.AVAILABLE, car.getStatus());
    }

    @Test
    public void testGetAllAvailableCars() {
        List<Car> cars = Arrays.asList(new Car(), new Car());
        when(carRepo.findByAvailableTrue()).thenReturn(cars);

        ResponseEntity<List<Car>> response = carService.getAllAvailableCars();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCarsByLocation() {
        List<Car> cars = Arrays.asList(new Car(), new Car());
        String location = "Chennai";
        when(carRepo.findByLocationAndAvailableTrue(location)).thenReturn(cars);

        ResponseEntity<List<Car>> response = carService.getCarsByLocation(location);
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateAvailability_Success() {
        Car car = new Car();
        car.setAvailable(true);
        car.setStatus(CarStatus.AVAILABLE);

        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        when(carRepo.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = carService.updateAvailability(1L, false);
        assertEquals("Car availability updated successfully.", response.getBody());
        assertFalse(car.isAvailable());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateAvailability_NotFound() {
        when(carRepo.findById(1L)).thenReturn(Optional.empty());
        carService.updateAvailability(1L, false);
    }

    @Test
    public void testGetAllCars() {
        List<Car> cars = Arrays.asList(new Car(), new Car(), new Car());
        when(carRepo.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();
        assertEquals(3, result.size());
    }

    @Test
    public void testDeleteCarById_Success() {
        when(carRepo.existsById(1L)).thenReturn(true);
        doNothing().when(carRepo).deleteById(1L);

        ResponseEntity<String> response = carService.deleteCarById(1L);
        assertEquals("Car deleted successfully.", response.getBody());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteCarById_NotFound() {
        when(carRepo.existsById(1L)).thenReturn(false);
        carService.deleteCarById(1L);
    }

    @Test
    public void testGetCarById_Success() {
        Car car = new Car();
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        ResponseEntity<Car> response = carService.getCarById(1L);
        assertNotNull(response.getBody());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetCarById_NotFound() {
        when(carRepo.findById(1L)).thenReturn(Optional.empty());
        carService.getCarById(1L);
    }
}

