package com.gateway.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import com.gateway.model.Request;

public class DatabaseManager {

    private Connection connection;
    private static DatabaseManager instance;

    private DatabaseManager(String url, String username, String password) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("There has been a problem");
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance(String url, String username, String password) {
        if (instance == null) {
            instance = new DatabaseManager(url, username, password);
        }
        return instance;
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
}