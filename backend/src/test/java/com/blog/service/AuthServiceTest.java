package com.blog.service;

import com.blog.dto.AuthRequest;
import com.blog.dto.AuthResponse;
import com.blog.dto.RegisterRequest;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");

        AuthResponse response = authService.register(request);

        assertNotNull(response.getToken());
        assertEquals("newuser", response.getUsername());
        assertEquals("newuser@example.com", response.getEmail());
        assertNotNull(response.getId());
        assertFalse(response.getRoles().isEmpty());
        assertTrue(response.getRoles().contains("ROLE_USER"));

        // Verify user was saved
        User savedUser = userRepository.findByUsername("newuser").orElseThrow();
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    void testRegisterDuplicateUsername() {
        // Create existing user
        User existing = new User();
        existing.setUsername("existing");
        existing.setEmail("existing@example.com");
        existing.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(existing);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Create existing user
        User existing = new User();
        existing.setUsername("existing");
        existing.setEmail("existing@example.com");
        existing.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(existing);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("existing@example.com");
        request.setPassword("password123");

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    void testLoginSuccess() {
        // Create user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        AuthResponse response = authService.login(request);

        assertNotNull(response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getId());
        assertFalse(response.getRoles().isEmpty());
    }

    @Test
    void testLoginInvalidCredentials() {
        // Create user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void testLoginUserNotFound() {
        AuthRequest request = new AuthRequest();
        request.setUsername("nonexistent");
        request.setPassword("password123");

        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> authService.login(request));
    }
}

