package com.blog.security;

import com.blog.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    private UserPrincipal userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.getRoles().add(User.Role.ROLE_USER);
        userPrincipal = UserPrincipal.create(user);
        authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
    }

    @Test
    void testGenerateToken() {
        String token = tokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateToken() {
        String token = tokenProvider.generateToken(authentication);

        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void testValidateInvalidToken() {
        assertFalse(tokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void testGetUserIdFromToken() {
        String token = tokenProvider.generateToken(authentication);

        Long userId = tokenProvider.getUserIdFromToken(token);

        assertEquals(1L, userId);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = tokenProvider.generateToken(authentication);

        String username = tokenProvider.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }
}

