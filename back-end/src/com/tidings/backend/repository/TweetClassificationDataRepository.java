package com.tidings.backend.repository;

import com.tidings.backend.domain.Tweet;
import org.jongo.MongoCollection;

public class TweetClassificationDataRepository extends Repository {

    private TweetClassificationDataRepository(String collectionName) {
        this.collectionName = collectionName;
    }

    public MongoCollection collection() {
        return jongo.getCollection(collectionName);
    }

    public Iterable<Tweet> all() {
        return collection().find().as(Tweet.class);
    }

    public static TweetClassificationDataRepository forTesting() {
        return new TweetClassificationDataRepository("test_data");
    }
}
