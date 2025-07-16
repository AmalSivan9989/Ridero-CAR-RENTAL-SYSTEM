package com.hexa.car.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCar(Car car);
}