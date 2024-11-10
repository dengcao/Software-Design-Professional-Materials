package com.mz.nlpservice.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by gaozhenan on 2016/8/10.
 */
public class BaseHelper {
    public MongoDatabase db;

    public BaseHelper(MongoDatabase db) {
        this.db = db;
    }

    protected MongoCollection<Document> getCollection(String colName) {
        return this.db.getCollection(colName);
    }
}
