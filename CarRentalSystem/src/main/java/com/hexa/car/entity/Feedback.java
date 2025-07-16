package com.hexa.car.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}