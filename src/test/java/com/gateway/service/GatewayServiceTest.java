package com.gateway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gateway.logging.Logger;
import com.gateway.security.ThreatDetector;
import com.gateway.database.DatabaseManager;
import com.gateway.model.Request;
import com.gateway.model.HttpMethod;

@ExtendWith(MockitoExtension.class)
public class GatewayServiceTest {

    @Mock
    ThreatDetector threatDetector;

    @Mock
    DatabaseManager databaseManager;

    @Mock
    Logger logger;

    @InjectMocks
    GatewayService gatewayService;

    @Test
    public void safeRequest_returnsTrue() {
        Request request = new Request("10.0.0.1", HttpMethod.POST, "/home", "normal content");
        when(threatDetector.isSafe(any())).thenReturn(true);
        assertTrue(gatewayService.handleRequest(request));
    }

    @Test
    public void unsafeRequest_returnsFalse() {
        Request request = new Request("192.168.1.1", HttpMethod.POST, "/login", "DROP TABLE users");
        when(threatDetector.isSafe(any())).thenReturn(false);
        assertFalse(gatewayService.handleRequest(request));
    }
}
