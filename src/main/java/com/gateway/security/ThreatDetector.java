package com.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gateway.model.HttpMethod;
import com.gateway.model.Request;

import jakarta.annotation.PostConstruct;

import com.gateway.database.DatabaseManager;
import java.util.List;

@Service
public class ThreatDetector {

    private List<String> blacklistedIPS;
    private List<String> dangerousKeyWords;
    private ThreatDataManager threatDataManager;
    private DatabaseManager dtb;
    @Value("${td.useDatabase}")
    private boolean useDatabase;

    public ThreatDetector(ThreatDataManager threatDataManager, DatabaseManager dtb) {
        this.threatDataManager = threatDataManager;
        this.dtb = dtb;
    }

    @PostConstruct
    public void init() {
        if (useDatabase) {
            if (dtb.getKeywords().isEmpty()) {
                dangerousKeyWords = threatDataManager.readDangerousKeywords();
                for (String keyword : dangerousKeyWords) {
                    dtb.insertKeyword(keyword);
                }
            }
            if (dtb.getBlacklist().isEmpty()) {
                blacklistedIPS = threatDataManager.readBlackList();
                for (String ip : blacklistedIPS) {
                    dtb.insertBlacklistedIP(ip);
                }
            }
            blacklistedIPS = dtb.getBlacklist();
            dangerousKeyWords = dtb.getKeywords();
        } else {
            blacklistedIPS = threatDataManager.readBlackList();
            dangerousKeyWords = threatDataManager.readDangerousKeywords();
        }
    }

    public boolean isSafe(Request request) {
        if (blacklistedIPS.contains(request.getIpAddress())) {
            return false;
        }
        if (request.getPayload() == null && request.getMethod() == HttpMethod.GET) {
            return true;
        }
        if (request.getPayload() == null && request.getMethod() != HttpMethod.GET) {
            throw new IllegalArgumentException("Payload cannot be null for " + request.getMethod() + " requests");
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
            dtb.insertBlacklistedIP(ip);
        }
    }

    public void addDangerousKeyword(String keyword) {
        if (dangerousKeyWords.contains(keyword)) {
            System.out.println("keyword is already present in the list");
        } else {
            dangerousKeyWords.add(keyword);
            threatDataManager.writeDangerousKeywords(dangerousKeyWords);
            dtb.insertKeyword(keyword);
        }
    }

    public boolean isBlacklisted(String ip) {
        return blacklistedIPS.contains(ip);
    }
}