package com.gateway.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.gateway.model.Request;
import com.gateway.database.DatabaseManager;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

@Component
public class Logger {

    private DatabaseManager dtb;

    public Logger(DatabaseManager dtb) {
        this.dtb = dtb;
    }

    @Value("${log.filePath}")
    private String filePath;

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