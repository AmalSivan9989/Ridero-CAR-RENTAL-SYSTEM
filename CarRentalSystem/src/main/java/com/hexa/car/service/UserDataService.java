package com.hexa.car.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexa.car.entity.UserData;
import com.hexa.car.repo.UserDataRepository;

@Service
public class UserDataService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserDataRepository userDataRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<UserData> userDetail=userDataRepository.findByName(username);
	System.out.println("Found user: " + userDetail.get().getName() + ", Password (hashed): " + userDetail.get().getPassword());
	

		return userDetail.map(UserDataDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found "+username));
	}
	
	
	public List<UserData> getAllUsers() {
    return userDataRepository.findAll();
}

public UserData getUserById(Integer id) {
    return userDataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
}

public UserData updateUser(Integer id, UserData newUserData) {
    UserData existing = getUserById(id);
    existing.setName(newUserData.getName());
    existing.setEmail(newUserData.getEmail());
    existing.setPassword(encoder.encode(newUserData.getPassword())); 
    existing.setRoles(newUserData.getRoles());
    existing.setActive(newUserData.isActive());
    return userDataRepository.save(existing);
}

public String deactivateUser(Integer id) {
    UserData user = getUserById(id);
    user.setActive(false);
    userDataRepository.save(user);
    return "User with ID " + id + " deactivated.";
}

	public UserData getUserDataByName(String username) {
    return userDataRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
}
public String addUser(UserData userInfo) {
   
    userInfo.setPassword(encoder.encode(userInfo.getPassword()));
    userInfo.setActive(true); 
    userDataRepository.save(userInfo);
    return "User added successfully";
}


	
}
