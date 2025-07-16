package com.hexa.car.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime pickupDate;
    private LocalDateTime dropoffDate;

    private String pickupLocation;
    private String dropoffLocation;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;


    private String status; 
    @Column(nullable = false)
private double totalAmount;

}
