package com.gateway.service;

import org.springframework.stereotype.Service;
import com.gateway.model.Request;
import com.gateway.model.StatsDTO;
import com.gateway.security.ThreatDetector;
import com.gateway.database.DatabaseManager;
import com.gateway.logging.Logger;
import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayService {
    private DatabaseManager dtb;
    private ThreatDetector detector;
    private Logger logger;

    private Map<String, Integer> incidentCount;

    public GatewayService(ThreatDetector detector, DatabaseManager dtb, Logger logger) {
        this.detector = detector;
        this.dtb = dtb;
        this.logger = logger;
        this.incidentCount = new HashMap<>();
    }

    public boolean handleRequest(Request request) {

        boolean result = detector.isSafe(request);
        logger.logAction(request, result);

        if (result) {
            System.out.println(">>> [SUCCESS] Request securely forwarded to target server.\n");
        } else {
            System.out.println(">>> [REJECTED] Request cancelled due to security reasons.\n");
            System.out.println(">>> Reason: Threat Detected.");

            String rejectedIpAddress = request.getIpAddress();
            if (!detector.isBlacklisted(rejectedIpAddress)) {
                int currentStrikes = incidentCount.getOrDefault(rejectedIpAddress, 0) + 1;
                incidentCount.put(rejectedIpAddress, currentStrikes);

                if (currentStrikes == 3) {
                    detector.addIPToBlacklist(rejectedIpAddress);
                    System.out.println("!!! [CRITICAL] IP " + rejectedIpAddress + " has been permanently blacklisted.");
                } else {
                    System.out.println(">>> Warning: Incident count for this IP: " + currentStrikes);
                }
            }
        }
        System.out.println("--------------------------------------------------");
        return result;
    }

    public StatsDTO showStats() {

        StatsDTO sDTO = new StatsDTO(dtb.getTotalRequests(), dtb.getBlockedRequests());
        return sDTO;
    }
}
