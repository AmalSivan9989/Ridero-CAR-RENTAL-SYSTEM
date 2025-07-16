package com.hexa.car.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.hexa.car.entity.AuthRequest;
import com.hexa.car.entity.UserData;
import com.hexa.car.security.JwtService;
import com.hexa.car.service.UserDataService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserDataService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome! This endpoint is not secured.";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserData userInfo) {
        return service.addUser(userInfo);
    }

   
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

  
    @GetMapping("/admin/getAllUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<UserData> users = service.getAllUsers();
        return ResponseEntity.ok(Map.of(
                "message", "Users fetched successfully",
                "total", users.size(),
                "data", users
        ));
    }

    
    @GetMapping("/admin/getUserById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UserData user = service.getUserById(id);
        return ResponseEntity.ok(Map.of(
                "message", "User fetched successfully",
                "user", user
        ));
    }

  
    @PutMapping("/admin/updateUser/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserData userData) {
        UserData updated = service.updateUser(id, userData);
        return ResponseEntity.ok(Map.of(
                "message", "User updated successfully",
                "user", updated
        ));
    }

    
    @PutMapping("/admin/deactivateUser/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        String result = service.deactivateUser(id);
        return ResponseEntity.ok(Map.of("message", result));
    }

   
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    
}
