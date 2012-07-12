package com.tidings.backend.repository;

import com.mongodb.Mongo;
import com.tidings.backend.NewsItem;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public class NewsItemsRepository {
    private static Jongo jongo;

    public NewsItemsRepository() {
        Mongo mongo = null;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        jongo = new Jongo(mongo.getDB("tidings_development"));
    }

    public void save(NewsItem newsItem) {
        collection().save(newsItem);
    }

    private MongoCollection collection() {
        return jongo.getCollection("news_items");
    }
}
