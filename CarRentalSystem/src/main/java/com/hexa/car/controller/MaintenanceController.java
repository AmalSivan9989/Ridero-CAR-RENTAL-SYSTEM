package com.hexa.car.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.service.MaintenanceService;

@RestController
@RequestMapping("/maintenance")
@CrossOrigin(origins = "http://localhost:4200")
public class MaintenanceController {

    @Autowired
    private MaintenanceService service;

   
    @PostMapping("/reportIssue/{carId}")
    public ResponseEntity<String> reportIssue(@PathVariable Long carId, @RequestBody MaintenanceRequest request) {
        return service.submitRequest(carId, request);
    }

    
    @GetMapping("/getRequestsByAgent/{agentId}")
    public ResponseEntity<?> getRequestsByAgent(@PathVariable Long agentId) {
        return service.getRequestsByAgent(agentId);
    }

    
    @PutMapping("/closeRequest/{requestId}/agent/{agentId}")
public ResponseEntity<String> closeRequest(
        @PathVariable Long requestId,
        @PathVariable Long agentId) {
    return service.closeRequest(requestId, agentId);
}

    @GetMapping("/requests")
    public ResponseEntity<List<MaintenanceRequest>> getRequests(@RequestParam Long agentId) {
        return service.getRequestsForAgentOrUnassigned(agentId);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignRequest(@RequestParam Long requestId, @RequestParam Long agentId) {
        return service.assignRequestToAgent(requestId, agentId);
    }

}
