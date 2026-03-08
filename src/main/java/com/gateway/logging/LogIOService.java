package com.gateway.logging;

//for imports, filepath is for which file we are importing.
//for exports, filepath is for where we are exporting.

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class LogIOService {

    public List<Map<String, String>> parseLogs(String logsFilePath) {
        List<Map<String, String>> logMapList = new ArrayList<>();
        try (FileReader fr = new FileReader(logsFilePath); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.replace("[", "").replace("]", "").split(" \\| ");
                Map<String, String> logMap = new HashMap<>();
                logMap.put("type", parts[0].trim());
                logMap.put("ip", parts[1].substring(parts[1].indexOf(":") + 1).trim());
                logMap.put("url", parts[2].substring(parts[2].indexOf(":") + 1).trim());
                logMap.put("method", parts[3].substring(parts[3].indexOf(":") + 1).trim());
                logMap.put("timestamp", parts[4].substring(parts[4].indexOf(":") + 1).trim());
                logMapList.add(logMap);
            }

        } catch (IOException e) {
            System.err.println("File reading error! Path: " + logsFilePath);
            e.printStackTrace();
        }

        return logMapList;
    }

    public List<Map<String, String>> importFromJSON(String importFilePath) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            ObjectMapper objmapper = new ObjectMapper();
            result = objmapper.readValue(new File(importFilePath), new TypeReference<List<Map<String, String>>>() {
            });
        } catch (IOException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
        return result;
    }

    public void exportFromJSON(String logsFilePath, String exportFilePath) {
        try {
            List<Map<String, String>> exportjsonlist = parseLogs(logsFilePath);
            ObjectMapper objmapper = new ObjectMapper();
            objmapper.writerWithDefaultPrettyPrinter().writeValue(new File(exportFilePath), exportjsonlist);
        } catch (IOException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> importFromCSV(String importFilePath) {
        List<Map<String, String>> logMapList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(importFilePath))) {
            String[] line;
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                Map<String, String> logMap = new HashMap<>();
                logMap.put("type", line[0]);
                logMap.put("ip", line[1]);
                logMap.put("url", line[2]);
                logMap.put("method", line[3]);
                logMap.put("timestamp", line[4]);
                logMapList.add(logMap);
            }

        } catch (IOException | CsvValidationException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
        return logMapList;
    }

    public void exportFromCSV(String logsFilePath, String exportFilePath) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(exportFilePath))) {
            String[] line = new String[] { "type", "ip", "url", "method", "timestamp" };
            csvWriter.writeNext(line);

            for (Map<String, String> map : parseLogs(logsFilePath)) {
                csvWriter.writeNext(new String[] { map.get("type"), map.get("ip"), map.get("url"), map.get("method"),
                        map.get("timestamp") });
            }

        } catch (IOException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> importFromXML(String importFilePath) {

        List<Map<String, String>> result = new ArrayList<>();
        try {
            XmlMapper XMLMapper = new XmlMapper();
            result = XMLMapper.readValue(new File(importFilePath), new TypeReference<List<Map<String, String>>>() {
            });
        } catch (IOException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
        return result;

    }

    public void exportFromXML(String logsFilePath, String exportFilePath) {
        try {
            List<Map<String, String>> exportjsonlist = parseLogs(logsFilePath);
            XmlMapper XMLMapper = new XmlMapper();
            XMLMapper.writerWithDefaultPrettyPrinter().writeValue(new File(exportFilePath), exportjsonlist);
        } catch (IOException e) {
            System.err.println("There has been a error");
            e.printStackTrace();
        }
    }
}
