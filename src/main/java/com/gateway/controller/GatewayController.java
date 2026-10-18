package com.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.gateway.logging.LogIOService;
import com.gateway.service.GatewayService;
import com.gateway.model.Request;
import com.gateway.model.RequestDTO;
import com.gateway.model.StatsDTO;
import com.gateway.security.ThreatDetector;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private GatewayService gatewayService;
    private ThreatDetector threatDetector;
    private LogIOService logIOService;

    @Value("${log.filePath}")
    private String filePath;
    @Value("${log.exportPath}")
    private String exportPath;

    public GatewayController(GatewayService gatewayService, ThreatDetector threatDetector, LogIOService logIOService) {
        this.gatewayService = gatewayService;
        this.threatDetector = threatDetector;
        this.logIOService = logIOService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> handleRequest(@RequestBody RequestDTO requestDTO) {
        Request request = new Request(requestDTO.getIpAddress(), requestDTO.getMethod(), requestDTO.getUrl(),
                requestDTO.getPayload());
        boolean result = gatewayService.handleRequest(request);
        if (result) {
            return ResponseEntity.ok("Request approved");
        } else {
            return ResponseEntity.status(403).body("Request blocked");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> getStats() {
        StatsDTO sDTO = gatewayService.showStats();
        return ResponseEntity.ok(sDTO);

    }

    @PostMapping("/blacklist")
    public ResponseEntity<String> addToBlackList(@RequestBody String ip) {
        threatDetector.addIPToBlacklist(ip);
        return ResponseEntity.ok("IP added to blacklist");
    }

    @PostMapping("/keywords")
    public ResponseEntity<String> addToKeywords(@RequestBody String word) {
        threatDetector.addDangerousKeyword(word);
        return ResponseEntity.ok("keyword added to list");
    }

    @GetMapping("/logs/export/json")
    public ResponseEntity<String> exportFromJSON() {
        logIOService.exportFromJSON(filePath, exportPath + ".json");
        return ResponseEntity.ok("exported as json");
    }

    @GetMapping("/logs/export/csv")
    public ResponseEntity<String> exportFromCSV() {
        logIOService.exportFromCSV(filePath, exportPath + ".csv");
        return ResponseEntity.ok("exported as csv");
    }

    @GetMapping("/logs/export/xml")
    public ResponseEntity<String> exportFromXML() {
        logIOService.exportFromXML(filePath, exportPath + ".xml");
        return ResponseEntity.ok("exported as xml");
    }

    @PostMapping("/logs/import/json")
    public ResponseEntity<List<Map<String, String>>> importFromJSON(@RequestParam String importPath) {
        return ResponseEntity.ok(logIOService.importFromJSON(importPath));
    }

    @PostMapping("/logs/import/csv")
    public ResponseEntity<List<Map<String, String>>> importFromCSV(@RequestParam String importPath) {
        return ResponseEntity.ok(logIOService.importFromCSV(importPath));
    }

    @PostMapping("/logs/import/xml")
    public ResponseEntity<List<Map<String, String>>> importFromXML(@RequestParam String importPath) {
        return ResponseEntity.ok(logIOService.importFromXML(importPath));
    }
}
