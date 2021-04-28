package com.sample.agent.utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.util.List;

public class MongoDb {

    final String CONNECTION = "54.254.209.206";
    final int PORT = 27017;

    private MongoClient dbClient;
    private DB db;

    public MongoDb() {
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
