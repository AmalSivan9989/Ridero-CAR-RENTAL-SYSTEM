package com.hexa.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.Feedback;
import com.hexa.car.service.FeedbackService;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

   
    @PostMapping("/submitFeedback")
    public ResponseEntity<String> submitFeedback(@RequestParam Long carId, @RequestBody Feedback feedback) {
        return feedbackService.submitFeedback(carId, feedback);
    }

    
    @GetMapping("/getFeedback/{carId}")
    public ResponseEntity<?> getFeedback(@PathVariable Long carId) {
        return feedbackService.getFeedbackForCar(carId);
    }

    
}
