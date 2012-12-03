package com.tidings.backend.repository;

import com.mongodb.Mongo;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public abstract class Repository {
    protected static Jongo jongo;
    protected String collectionName;
    private final TidingsEnvironment.DBConfig dbConfig;


    public Repository() {
        dbConfig = TidingsEnvironment.getInstance().dbConfig();
        Mongo mongo;
        try {
            mongo = new Mongo(dbConfig.host(), dbConfig.port());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        jongo = new Jongo(mongo.getDB(dbConfig.database()));
    }

    public void save(Object entity) {
        collection().save(entity);
    }

    protected abstract MongoCollection collection();
}