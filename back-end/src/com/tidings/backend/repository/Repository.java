package com.tidings.backend.repository;

import com.mongodb.Mongo;
import org.jongo.Jongo;

import java.net.UnknownHostException;

public class Repository {
    protected static Jongo jongo;

    public Repository() {
        Mongo mongo;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        jongo = new Jongo(mongo.getDB("tidings_development"));
    }
}
