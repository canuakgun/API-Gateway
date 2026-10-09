package com.gateway.model;

import java.time.LocalDateTime;

public class Request {

    private final String ipAddress;
    private final HttpMethod method;
    private final String url;
    private final String payload;
    private final LocalDateTime timestamp;

    public Request(String ipAddress, HttpMethod method, String url, String payload) {
        this.ipAddress = ipAddress;
        this.method = method;
        this.url = url;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - IP: %s", timestamp, method, url, ipAddress);
    }
}
