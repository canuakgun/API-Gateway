package com.gateway.logging;

import com.gateway.model.Request;
import com.gateway.database.DatabaseManager;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Logger {

    private DatabaseManager dtb;

    public Logger(DatabaseManager dtb) {
        this.dtb = dtb;
    }

    String filePath = "logs/security_logs.txt";

    public void logAction(Request request, boolean isSafe) {

        try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw)) {
            String label = isSafe ? "[APPROVED] | " : "[BLOCKED] | ";

            bw.write(label + " ip: " + request.getIpAddress() + " | url: " + request.getUrl() +
                    " | method: " + request.getMethod() + " | timestamp: " + request.getTimestamp());
            bw.newLine();
            dtb.insertLog(request, isSafe);
        } catch (IOException e) {
            System.err.println("File writing error! Path: " + filePath);
            e.printStackTrace();
        }
    }
}