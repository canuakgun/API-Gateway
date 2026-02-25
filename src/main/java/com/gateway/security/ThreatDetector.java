package com.gateway.security;

import java.util.List;

import com.gateway.model.Request;

import java.util.ArrayList;

public class ThreatDetector {

    private List<String> blacklistedIPS;
    private List<String> dangerousKeyWords;

    public ThreatDetector() {
        this.blacklistedIPS = new ArrayList<>();
        this.dangerousKeyWords = new ArrayList<>();

        blacklistedIPS.add("192.168.1.1");
        dangerousKeyWords.add("DROP");
        dangerousKeyWords.add("SELECT");
    }

    public boolean isSafe(Request request) {
        if (blacklistedIPS.contains(request.getIpAddress()) == true) {
            return false;
        }
        String upperPayload = request.getPayload().toUpperCase();
        String upperURL = request.getUrl().toUpperCase();

        for (String word : dangerousKeyWords) {
            if (upperPayload.contains(word) || upperURL.contains(word)) {
                return false;
            }
        }
        return true;
    }

    public void addIPToBlacklist(String ip) {
        blacklistedIPS.add(ip);
    }
}