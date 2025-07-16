package com.hexa.car;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hexa.car.entity.UserData;
import com.hexa.car.repo.UserDataRepository;
import com.hexa.car.service.UserDataService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDataServiceTest {

    @InjectMocks
    private UserDataService userDataService;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        UserData user = new UserData();
        user.setUid(1);
        user.setName("john");
        user.setPassword("encodedPass");
        user.setRoles("ROLE_USER");

        when(userDataRepository.findByName("john")).thenReturn(Optional.of(user));

        org.springframework.security.core.userdetails.UserDetails userDetails = userDataService.loadUserByUsername("john");

        assertEquals("john", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        verify(userDataRepository).findByName("john");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_UserNotFound() {
        when(userDataRepository.findByName("unknown")).thenReturn(Optional.empty());
        userDataService.loadUserByUsername("unknown");
    }

    @Test
    public void testGetAllUsers() {
        List<UserData> users = Arrays.asList(
            new UserData(1, "Alice", "alice@example.com", "pass", "ROLE_USER", true),
            new UserData(2, "Bob", "bob@example.com", "pass", "ROLE_ADMIN", true)
        );
        when(userDataRepository.findAll()).thenReturn(users);

        List<UserData> result = userDataService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDataRepository).findAll();
    }

    @Test
    public void testGetUserById_Found() {
        UserData user = new UserData(1, "Alice", "alice@example.com", "pass", "ROLE_USER", true);
        when(userDataRepository.findById(1)).thenReturn(Optional.of(user));

        UserData result = userDataService.getUserById(1);

        assertEquals("Alice", result.getName());
        verify(userDataRepository).findById(1);
    }

    @Test(expected = RuntimeException.class)
    public void testGetUserById_NotFound() {
        when(userDataRepository.findById(1)).thenReturn(Optional.empty());
        userDataService.getUserById(1);
    }

    @Test
    public void testUpdateUser() {
        UserData existingUser = new UserData(1, "OldName", "old@example.com", "oldPass", "ROLE_USER", true);
        UserData newUserData = new UserData();
        newUserData.setName("NewName");
        newUserData.setEmail("new@example.com");
        newUserData.setPassword("newPass");
        newUserData.setRoles("ROLE_ADMIN");
        newUserData.setActive(false);

        when(userDataRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userDataRepository.save(any(UserData.class))).thenAnswer(i -> i.getArguments()[0]);

        UserData updated = userDataService.updateUser(1, newUserData);

        assertEquals("NewName", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals("encodedNewPass", updated.getPassword());
        assertEquals("ROLE_ADMIN", updated.getRoles());
        assertFalse(updated.isActive());

        verify(userDataRepository).findById(1);
        verify(passwordEncoder).encode("newPass");
        verify(userDataRepository).save(existingUser);
    }

    @Test
    public void testDeactivateUser() {
        UserData user = new UserData(1, "User", "user@example.com", "pass", "ROLE_USER", true);

        when(userDataRepository.findById(1)).thenReturn(Optional.of(user));
        when(userDataRepository.save(user)).thenReturn(user);

        String result = userDataService.deactivateUser(1);

        assertEquals("User with ID 1 deactivated.", result);
        assertFalse(user.isActive());

        verify(userDataRepository).findById(1);
        verify(userDataRepository).save(user);
    }

    @Test
    public void testAddUser() {
        UserData user = new UserData();
        user.setPassword("plainPass");

        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
        when(userDataRepository.save(any(UserData.class))).thenAnswer(i -> i.getArguments()[0]);

        String result = userDataService.addUser(user);

        assertEquals("encodedPass", user.getPassword());
        assertTrue(user.isActive());
        assertEquals("User added successfully", result);

        verify(passwordEncoder).encode("plainPass");
        verify(userDataRepository).save(user);
    }
}
