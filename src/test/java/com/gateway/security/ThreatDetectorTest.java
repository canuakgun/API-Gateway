package com.gateway.security;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import com.gateway.database.DatabaseManager;
import com.gateway.security.ThreatDataManager;
import com.gateway.security.ThreatDetector;
import com.gateway.model.Request;
import com.gateway.model.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ThreatDetectorTest {

    @Mock
    DatabaseManager databaseManager;

    @Mock
    ThreatDataManager threatDataManager;

    @InjectMocks
    ThreatDetector threatDetector;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(threatDetector, "useDatabase", true);
        when(databaseManager.getKeywords()).thenReturn(new ArrayList<>());
        when(databaseManager.getBlacklist()).thenReturn(new ArrayList<>());

        threatDetector.init();
    }

    @Test
    public void isSafe_cleanRequest_returnsTrue() {
        Request request = new Request("10.0.0.2", HttpMethod.GET, "/dashboard", "normal content");
        assertTrue(threatDetector.isSafe(request));
    }

    @Test
    public void isSafe_blacklistedIP_returnsFalse() {
        threatDetector.addIPToBlacklist("192.168.1.1");
        Request request = new Request("192.168.1.1", HttpMethod.GET, "/home", null);
        assertFalse(threatDetector.isSafe(request));
    }

    @Test
    public void isSafe_dangerousKeyword_returnsFalse() {
        threatDetector.addDangerousKeyword("DROP");
        Request request = new Request("10.0.0.1", HttpMethod.POST, "/search", "DROP TABLE users");
        assertFalse(threatDetector.isSafe(request));
    }
}
