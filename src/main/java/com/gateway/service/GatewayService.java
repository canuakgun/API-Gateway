package com.gateway.service;

import com.gateway.model.Request;
import com.gateway.security.ThreatDetector;
import com.gateway.logging.Logger;
import java.util.HashMap;
import java.util.Map;

public class GatewayService {
    private ThreatDetector detector;
    private Logger logger;

    private int totalRequests = 0;
    private int blockedRequests = 0;

    private Map<String, Integer> incidentCount;

    public GatewayService(ThreatDetector detector, Logger logger) {
        this.detector = detector;
        this.logger = logger;
        this.incidentCount = new HashMap<>();
    }

    public void handleRequest(Request request) {
        totalRequests++;

        boolean result = detector.isSafe(request);
        logger.logAction(request, result);

        if (result) {
            System.out.println(">>> [SUCCESS] Request securely forwarded to target server.\n");
        } else {
            blockedRequests++;
            System.out.println(">>> [REJECTED] Request cancelled due to security reasons.\n");
            System.out.println(">>> Reason: Threat Detected.");

            String rejectedIpAddress = request.getIpAddress();
            int currentStrikes = incidentCount.getOrDefault(rejectedIpAddress, 0) + 1;
            incidentCount.put(rejectedIpAddress, currentStrikes);

            if (currentStrikes >= 3) {
                detector.addIPToBlacklist(rejectedIpAddress);
                System.out.println("!!! [CRITICAL] IP " + rejectedIpAddress + " has been permanently blacklisted.");
            } else {
                System.out.println(">>> Warning: Incident count for this IP: " + currentStrikes);
            }
        }
        System.out.println("--------------------------------------------------");
    }

    public void showStats() {
        System.out.println("\n======= GATEWAY DAILY REPORT =======");
        System.out.println("Total Requests Processed : " + totalRequests);
        System.out.println("Total Attacks Blocked    : " + blockedRequests);
        System.out.println("Safe Passages            : " + (totalRequests - blockedRequests));
        System.out.println("=====================================\n");
    }
}
