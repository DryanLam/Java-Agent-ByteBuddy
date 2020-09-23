package com.sample.agent.utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.util.List;

public class MongoDb {

    final String CONNECTION = "localhost";
    final int PORT = 27017;

    MongoClient dbClient;
    DB db;

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
