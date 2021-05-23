package com.sample.agent.utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.util.List;
import java.util.Properties;

public class MongoDb {

    private String CONNECTION = "localhost";
    final int PORT = 27017;

    private MongoClient dbClient;
    private DB db;

    public MongoDb() {
        Properties prop = System.getProperties();
        String host = prop.getProperty("mongodb.host");
        if(host == null){
            host = "localhost";
        }
        CONNECTION = host;
        System.out.println(CONNECTION);
        dbClient = new MongoClient(CONNECTION, PORT);
    }

    public DB getDB(String dbName) {
        db = dbClient.getDB(dbName);
        return db;
    }

    public MongoClient getClient() {
        return dbClient;
    }

    public List getCollections() {
        return dbClient.getDatabaseNames();
    }
}
