package com.hexa.car.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexa.car.entity.UserData;

public interface UserDataRepository extends JpaRepository<UserData, Integer> {

	Optional<UserData>findByName(String username);
	
}
