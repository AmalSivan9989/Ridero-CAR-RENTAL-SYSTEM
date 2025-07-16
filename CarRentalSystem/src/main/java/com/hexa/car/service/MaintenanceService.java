package com.hexa.car.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.Car;
import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.repo.MaintenanceRequestRepository;
import com.hexa.car.repo.UserDataRepository;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRequestRepository requestRepo;

    @Autowired
    private UserDataRepository userDataRepo;

    @Autowired
    private CarRepository carRepo;

    public ResponseEntity<String> submitRequest(Long carId, MaintenanceRequest request) {
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + carId));

        request.setCar(car);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("OPEN");

        requestRepo.save(request);

        return ResponseEntity.ok("Maintenance request submitted successfully for Car ID " + carId);
    }

    public ResponseEntity<List<MaintenanceRequest>> getRequestsByAgent(Long agentId) {
        UserData agent = userDataRepo.findById(agentId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

        List<MaintenanceRequest> requests = requestRepo.findByAgent(agent);
        return ResponseEntity.ok(requests);
    }

    
    public ResponseEntity<String> closeRequest(Long requestId, Long agentId) {
    MaintenanceRequest request = requestRepo.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Maintenance Request not found with ID: " + requestId));

    UserData agent = userDataRepo.findById(agentId.intValue())
            .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

    request.setStatus("CLOSED");
    request.setAgent(agent); 
    request.setRequestDate(LocalDateTime.now()); 

    requestRepo.save(request);

    return ResponseEntity.ok("Maintenance request with ID " + requestId + " closed successfully by Agent ID " + agentId);
}



public ResponseEntity<List<MaintenanceRequest>> getRequestsForAgentOrUnassigned(Long agentId) {
    UserData agent = userDataRepo.findById(agentId.intValue())
            .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

    List<MaintenanceRequest> requests = requestRepo.findByAgentOrAgentIsNullAndStatusNot(agent, "CLOSED");
    return ResponseEntity.ok(requests);
}



public ResponseEntity<String> assignRequestToAgent(Long requestId, Long agentId) {
    MaintenanceRequest request = requestRepo.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Maintenance Request not found with ID: " + requestId));

    if (request.getAgent() != null) {
        return ResponseEntity.badRequest().body("Request is already assigned");
    }

    UserData agent = userDataRepo.findById(agentId.intValue())
            .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

    request.setAgent(agent);
    requestRepo.save(request);

    return ResponseEntity.ok("Request assigned to agent successfully");
}

}
