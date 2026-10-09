package com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gateway.service.GatewayService;
import com.gateway.model.Request;
import com.gateway.model.RequestDTO;
import com.gateway.model.StatsDTO;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
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

}
