package com.gateway.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import com.gateway.model.Request;

@Repository
public class DatabaseManager {

    private Connection connection;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    @PostConstruct
    public void init() {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
    }

    public void insertLog(Request request, boolean isSafe) {
        String sql = "INSERT INTO logs (status, ip, url, http_method, log_timestamp) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, isSafe ? "APPROVED" : "BLOCKED");
            ps.setString(2, request.getIpAddress());
            ps.setString(3, request.getUrl());
            ps.setString(4, request.getMethod().toString());
            ps.setString(5, request.getTimestamp().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
    }

    public void insertBlacklistedIP(String ip) {
        String sql = "INSERT INTO blacklist (blacklistedips) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
    }

    public List<String> getBlacklist() {
        String sql = "SELECT blacklistedips FROM blacklist";
        List<String> ipList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String line = rs.getString("blacklistedips");
                ipList.add(line);
            }
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
        return ipList;
    }

    public void insertKeyword(String word) {
        String sql = "INSERT INTO keywords (dangerouswords) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, word);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
    }

    public List<String> getKeywords() {
        String sql = "SELECT dangerouswords FROM keywords";
        List<String> wordList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String line = rs.getString("dangerouswords");
                wordList.add(line);
            }
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
        return wordList;
    }

    public int getTotalRequests() {
        String sql = "SELECT COUNT(*) FROM logs";

        try {
            int requestCount = 0;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                requestCount = rs.getInt(1);
            }
            return requestCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getBlockedRequests() {
        String sql = "SELECT COUNT(*) FROM logs WHERE status = 'BLOCKED'";

        try {
            int blockedRequestcount = 0;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blockedRequestcount = rs.getInt(1);
            }
            return blockedRequestcount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}