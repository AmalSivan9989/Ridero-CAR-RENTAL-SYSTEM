package com.hexa.car.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import com.hexa.car.entity.UserData;
import com.hexa.car.repo.UserDataRepository;
import com.hexa.car.service.BookingService;

@RestController
@RequestMapping("/agent")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentController {

    @Autowired
    private BookingService bookingService;
    @Autowired
	private UserDataRepository userDataRepository;

    @PutMapping("/checkin/{bookingId}")
    public ResponseEntity<String> checkIn(@PathVariable Long bookingId) {
        return bookingService.checkIn(bookingId);
    }

    @PutMapping("/checkout/{bookingId}")
    public ResponseEntity<String> checkOut(@PathVariable Long bookingId) {
        return bookingService.checkOut(bookingId); 
    }
    @GetMapping("/getAllAgents")
public ResponseEntity<List<UserData>> getAllAgents() {
    List<UserData> agents = userDataRepository.findAll()
        .stream()
        .filter(user -> user.getRoles() != null && user.getRoles().equalsIgnoreCase("ROLE_AGENT"))
        .collect(Collectors.toList());

    return ResponseEntity.ok(agents);
}
@GetMapping("/getByName/{username}")
    @PreAuthorize("hasAuthority('ROLE_AGENT')")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
       Optional<UserData> optionalUser = userDataRepository.findByName(username);


        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

