package com.tidings.backend.repository;

import org.jongo.MongoCollection;

public class TweetsTrainingRepository extends Repository {
    public MongoCollection collection() {
        return jongo.getCollection("tweets_training_data");
    }
}
