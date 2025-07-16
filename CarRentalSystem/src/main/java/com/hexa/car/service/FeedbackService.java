package com.hexa.car.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.Feedback;
import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.repo.FeedbackRepository;
import com.hexa.car.repo.MaintenanceRequestRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private CarRepository carRepo;

    @Autowired
    private MaintenanceRequestRepository maintenanceRepo;

    public ResponseEntity<String> submitFeedback(Long carId, Feedback feedback) {
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + carId));

        feedback.setCar(car);
        feedbackRepo.save(feedback);

        
        if (feedback.getRating() <= 2) {
            MaintenanceRequest request = new MaintenanceRequest();
            request.setCar(car);
            request.setIssueDescription("Feedback Issue: " + feedback.getComment());
            request.setRequestDate(LocalDateTime.now());
            request.setStatus("OPEN");

            maintenanceRepo.save(request);
        }

        return ResponseEntity.ok("Feedback submitted successfully for car ID " + carId);
    }

    public ResponseEntity<List<Feedback>> getFeedbackForCar(Long carId) {
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + carId));
        List<Feedback> feedbackList = feedbackRepo.findByCar(car);
        return ResponseEntity.ok(feedbackList);
    }


}
