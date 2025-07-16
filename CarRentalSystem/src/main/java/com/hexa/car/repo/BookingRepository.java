package com.hexa.car.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.Booking;
import com.hexa.car.entity.UserData;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(UserData user);
    List<Booking> findByUser_UidAndStatus(Integer uid, String status);

}