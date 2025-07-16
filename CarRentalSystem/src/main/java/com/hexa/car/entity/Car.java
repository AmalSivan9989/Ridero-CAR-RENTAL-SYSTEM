package com.hexa.car.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private String location;
    private String type; 

    private String imageUrl;

    private double pricePerDay;

    private boolean available = true;
    
    @Enumerated(EnumType.STRING)
    private CarStatus status;  

}