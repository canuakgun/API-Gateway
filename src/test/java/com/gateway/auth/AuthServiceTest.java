package com.gateway.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    AuthService authService = new AuthService();

    @BeforeEach
    public void setup() {

        ReflectionTestUtils.setField(authService, "authUsername", "admin");
        ReflectionTestUtils.setField(authService, "authPassword", "admin123");
    }

    @Test
    public void authenticate_validCredentials_returnsTrue() {
        assertTrue(authService.authenticate("admin", "admin123"));

    }

    @Test
    public void authenticate_validCredentials_returnsFalse() {
        assertFalse(authService.authenticate("admin123", "admin"));
    }
}
