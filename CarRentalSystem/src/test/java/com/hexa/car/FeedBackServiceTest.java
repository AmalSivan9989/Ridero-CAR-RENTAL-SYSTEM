package com.hexa.car;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.Feedback;
import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.repo.FeedbackRepository;
import com.hexa.car.repo.MaintenanceRequestRepository;
import com.hexa.car.service.FeedbackService;

public class FeedBackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepo;

    @Mock
    private CarRepository carRepo;

    @Mock
    private MaintenanceRequestRepository maintenanceRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitFeedback_WithLowRating_CreatesMaintenanceRequest() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        Feedback feedback = new Feedback();
        feedback.setRating(2);
        feedback.setComment("Bad experience");

        when(carRepo.findById(carId)).thenReturn(Optional.of(car));
        when(feedbackRepo.save(feedback)).thenReturn(feedback);
        when(maintenanceRepo.save(any(MaintenanceRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = feedbackService.submitFeedback(carId, feedback);

        assertEquals("Feedback submitted successfully for car ID " + carId, response.getBody());
        verify(feedbackRepo).save(feedback);
        verify(maintenanceRepo).save(any(MaintenanceRequest.class));
    }

    @Test
    public void testSubmitFeedback_WithHighRating_NoMaintenanceRequest() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        Feedback feedback = new Feedback();
        feedback.setRating(4);
        feedback.setComment("Good car");

        when(carRepo.findById(carId)).thenReturn(Optional.of(car));
        when(feedbackRepo.save(feedback)).thenReturn(feedback);

        ResponseEntity<String> response = feedbackService.submitFeedback(carId, feedback);

        assertEquals("Feedback submitted successfully for car ID " + carId, response.getBody());
        verify(feedbackRepo).save(feedback);
        verify(maintenanceRepo, never()).save(any(MaintenanceRequest.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSubmitFeedback_CarNotFound() {
        Long carId = 1L;
        Feedback feedback = new Feedback();

        when(carRepo.findById(carId)).thenReturn(Optional.empty());
        feedbackService.submitFeedback(carId, feedback);
    }

    @Test
    public void testGetFeedbackForCar_Success() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        List<Feedback> feedbackList = Arrays.asList(
                new Feedback(), new Feedback()
        );

        when(carRepo.findById(carId)).thenReturn(Optional.of(car));
        when(feedbackRepo.findByCar(car)).thenReturn(feedbackList);

        ResponseEntity<List<Feedback>> response = feedbackService.getFeedbackForCar(carId);

        assertEquals(2, response.getBody().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetFeedbackForCar_CarNotFound() {
        Long carId = 1L;
        when(carRepo.findById(carId)).thenReturn(Optional.empty());
        feedbackService.getFeedbackForCar(carId);
    }
}
