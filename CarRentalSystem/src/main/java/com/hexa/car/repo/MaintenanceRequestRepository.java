package com.hexa.car.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.entity.UserData;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByCar(Car car);
    List<MaintenanceRequest> findByAgent(UserData agent);
    List<MaintenanceRequest> findByAgentOrAgentIsNullAndStatusNot(UserData agent, String status);
}