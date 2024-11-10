package com.mz.nlpservice.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mz.nlpservice.core.Context;

/**
 * Created by meizu on 2016/8/5.
 */
public class DBFactory {

    private static MongoClient musicClient;
    private static MongoDatabase musicDB;

    static {
        MongoClientURI connectionString = new MongoClientURI(Context.getInstance().getProperty("mongo_url"));
        musicClient = new MongoClient(connectionString);
        musicDB = musicClient.getDatabase("SmartDevicesCMS");
    }

    public  static MongoDatabase getMusicDBClient() {
        return musicDB;
    }


}
