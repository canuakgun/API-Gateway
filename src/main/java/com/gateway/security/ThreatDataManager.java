package com.gateway.security;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ThreatDataManager {

    private String blacklistPath = "logs/blacklist.txt";
    private String keywordsPath = "logs/keywords.txt";

    public ThreatDataManager(String blacklistPath, String keywordsPath) {
        this.blacklistPath = blacklistPath;
        this.keywordsPath = keywordsPath;
    }

    public List<String> readBlackList() {
        List<String> blList = new ArrayList<>();
        try (FileReader fr = new FileReader(blacklistPath); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                blList.add(line);
            }
        } catch (IOException e) {
            System.err.println("File reading error! Path: " + blacklistPath);
            e.printStackTrace();
        }
        return blList;
    }

    public void writeBlacklist(List<String> blacklist) {
        try (FileWriter fw = new FileWriter(blacklistPath, false); BufferedWriter bw = new BufferedWriter(fw)) {
            for (String line : blacklist) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("File writing error! Path: " + blacklistPath);
            e.printStackTrace();
        }
    }

    public List<String> readDangerousKeywords() {
        List<String> keywordList = new ArrayList<>();
        try (FileReader fr = new FileReader(keywordsPath); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                keywordList.add(line);
            }
        } catch (IOException e) {
            System.err.println("File reading error! Path: " + keywordsPath);
            e.printStackTrace();
        }
        return keywordList;
    }

    public void writeDangerousKeywords(List<String> keywords) {
        try (FileWriter fw = new FileWriter(keywordsPath, false); BufferedWriter bw = new BufferedWriter(fw)) {
            for (String line : keywords) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("File writing error! Path: " + keywordsPath);
            e.printStackTrace();
        }
    }

}
