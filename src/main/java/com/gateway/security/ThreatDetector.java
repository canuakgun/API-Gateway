package com.gateway.security;

import com.gateway.model.Request;
import java.util.List;

public class ThreatDetector {

    private List<String> blacklistedIPS;
    private List<String> dangerousKeyWords;
    private ThreatDataManager threatDataManager;

    public ThreatDetector(ThreatDataManager threatDataManager) {
        this.threatDataManager = threatDataManager;
        blacklistedIPS = threatDataManager.readBlackList();
        dangerousKeyWords = threatDataManager.readDangerousKeywords();
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
        if (blacklistedIPS.contains(ip)) {
            System.out.println("ip is already present in the list");
        } else {
            blacklistedIPS.add(ip);
            threatDataManager.writeBlacklist(blacklistedIPS);
        }
    }

    public void addDangerousKeyword(String keyword) {
        if (dangerousKeyWords.contains(keyword)) {
            System.out.println("keyword is already present in the list");
        } else {
            dangerousKeyWords.add(keyword);
            threatDataManager.writeDangerousKeywords(dangerousKeyWords);
        }
    }

    public boolean isBlacklisted(String ip) {
        return blacklistedIPS.contains(ip);
    }
}