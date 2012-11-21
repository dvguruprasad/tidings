package com.tidings.backend.repository;

import org.jongo.MongoCollection;

public class TweetsRepository extends Repository {
    @Override
    protected MongoCollection collection() {
        return jongo.getCollection("tweets");
    }
}
