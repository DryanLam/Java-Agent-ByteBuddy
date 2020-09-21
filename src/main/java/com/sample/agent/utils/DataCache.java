package com.sample.agent.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataCache {

    private static DataCache INSTANCE = null;

    public String STATUS;
    public Map dataCaching;

    private DataCache() {
        dataCaching = new LinkedHashMap();
    }

    public static DataCache getInstance() {
        // To ensure only one instance is created
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    public void addData(String key, String value) {
        System.out.println("Add: " + key + " Value: " + value);
        dataCaching.put(key, value);
    }

    public Map getData(){
        return dataCaching;
    }

    public void reset() {
        dataCaching.clear();
        System.out.println("Cleared cache");
    }

    public String getStatus(){
        return STATUS;
    }

    public void updateStatus(String status){
        STATUS = status;
    }

}
