package com.gateway.model;

public class StatsDTO {

    private int totalRequests;
    private int blockedRequests;
    private int safePassages;

    public StatsDTO(int totalRequests, int blockedRequests) {
        this.totalRequests = totalRequests;
        this.blockedRequests = blockedRequests;
        this.safePassages = totalRequests - blockedRequests;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getBlockedRequests() {
        return blockedRequests;
    }

    public int getSafePassages() {
        return safePassages;
    }
}