package com.tidings.backend.repository;

import com.mongodb.Mongo;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public abstract class Repository {
    protected static Jongo jongo;
    protected String collectionName;


    public Repository() {
        Mongo mongo;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        jongo = new Jongo(mongo.getDB("tidings_development"));
    }

    public void save(Object entity) {
        collection().save(entity);
    }

    protected abstract MongoCollection collection();
}