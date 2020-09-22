package com.sample.agent.utils;

import java.util.*;

public class DataCache {

    private static DataCache INSTANCE = null;

    public String STATUS;
    public Map dataCaching;
    public Set methodCatching;

    private DataCache() {
        dataCaching = new LinkedHashMap();
        methodCatching = new TreeSet<>();
    }

    public static DataCache getInstance() {
        // To ensure only one instance is created
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    public void addMethod(String coverMethod){
        System.out.println("Add method: " + coverMethod);
        methodCatching.add(coverMethod);
    }

    public Set getMethods(){
        return methodCatching;
    }

    public void addData(String key, Object value) {
        System.out.println("Add: " + key + " Value: " + value);
        dataCaching.put(key, value);
    }

    public Map getData(){
        return dataCaching;
    }

    public void reset() {
        dataCaching.clear();
        methodCatching.clear();
        System.out.println("Cleared cache");
    }

    public String getStatus(){
        return STATUS;
    }

    public void updateStatus(String status){
        STATUS = status;
    }

}
