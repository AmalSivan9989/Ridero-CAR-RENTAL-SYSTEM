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
import com.hexa.car.entity.MaintenanceRequest;
import com.hexa.car.entity.UserData;
import com.hexa.car.exception.ResourceNotFoundException;
import com.hexa.car.repo.CarRepository;
import com.hexa.car.repo.MaintenanceRequestRepository;
import com.hexa.car.repo.UserDataRepository;
import com.hexa.car.service.MaintenanceService;

public class MaintenanceServiceTest {

    @InjectMocks
    private MaintenanceService maintenanceService;

    @Mock
    private MaintenanceRequestRepository requestRepo;

    @Mock
    private UserDataRepository userDataRepo;

    @Mock
    private CarRepository carRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitRequest_Success() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        MaintenanceRequest request = new MaintenanceRequest();
        request.setIssueDescription("Engine problem");

        when(carRepo.findById(carId)).thenReturn(Optional.of(car));
        when(requestRepo.save(any(MaintenanceRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = maintenanceService.submitRequest(carId, request);

        assertEquals("Maintenance request submitted successfully for Car ID " + carId, response.getBody());
        assertEquals(car, request.getCar());
        assertEquals("OPEN", request.getStatus());
        assertNotNull(request.getRequestDate());
        verify(requestRepo).save(request);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSubmitRequest_CarNotFound() {
        Long carId = 1L;
        MaintenanceRequest request = new MaintenanceRequest();

        when(carRepo.findById(carId)).thenReturn(Optional.empty());
        maintenanceService.submitRequest(carId, request);
    }

    @Test
    public void testGetRequestsByAgent_Success() {
        Long agentId = 10L;
        UserData agent = new UserData();
        agent.setUid(agentId.intValue());

        List<MaintenanceRequest> requests = Arrays.asList(new MaintenanceRequest(), new MaintenanceRequest());

        when(userDataRepo.findById(agentId.intValue())).thenReturn(Optional.of(agent));
        when(requestRepo.findByAgent(agent)).thenReturn(requests);

        ResponseEntity<List<MaintenanceRequest>> response = maintenanceService.getRequestsByAgent(agentId);

        assertEquals(2, response.getBody().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetRequestsByAgent_AgentNotFound() {
        Long agentId = 10L;

        when(userDataRepo.findById(agentId.intValue())).thenReturn(Optional.empty());
        maintenanceService.getRequestsByAgent(agentId);
    }

    @Test
    public void testCloseRequest_Success() {
        Long requestId = 100L;
        Long agentId = 10L;

        MaintenanceRequest request = new MaintenanceRequest();
        request.setStatus("OPEN");

        UserData agent = new UserData();
        agent.setUid(agentId.intValue());

        when(requestRepo.findById(requestId)).thenReturn(Optional.of(request));
        when(userDataRepo.findById(agentId.intValue())).thenReturn(Optional.of(agent));
        when(requestRepo.save(any(MaintenanceRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<String> response = maintenanceService.closeRequest(requestId, agentId);

        assertEquals("Maintenance request with ID " + requestId + " closed successfully by Agent ID " + agentId, response.getBody());
        assertEquals("CLOSED", request.getStatus());
        assertEquals(agent, request.getAgent());
        assertNotNull(request.getRequestDate());
        verify(requestRepo).save(request);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCloseRequest_RequestNotFound() {
        Long requestId = 100L;
        Long agentId = 10L;

        when(requestRepo.findById(requestId)).thenReturn(Optional.empty());
        maintenanceService.closeRequest(requestId, agentId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCloseRequest_AgentNotFound() {
        Long requestId = 100L;
        Long agentId = 10L;

        MaintenanceRequest request = new MaintenanceRequest();

        when(requestRepo.findById(requestId)).thenReturn(Optional.of(request));
        when(userDataRepo.findById(agentId.intValue())).thenReturn(Optional.empty());

        maintenanceService.closeRequest(requestId, agentId);
    }
}

