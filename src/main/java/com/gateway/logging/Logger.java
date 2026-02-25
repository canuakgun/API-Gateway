package com.gateway.logging;

import com.gateway.model.Request;

public class Logger {

    public Logger() {

    }

    public void logAction(Request request, boolean isSafe) {
        if (!isSafe) {
            System.out.println("[SECURITY VIOLATION] Blocked: " + request.getIpAddress() +
                    " | Method: " + request.getMethod() +
                    " | URL: " + request.getUrl());
        } else {
            System.out.println("[INFORMATION] Request Approved: " + request.getIpAddress() +
                    " -> " + request.getUrl());
        }
    }
}
