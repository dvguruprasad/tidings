package com.tidings.backend.repository;

import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsItem;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class TrainingRepository extends Repository {

    public MongoCollection collection() {
        return jongo.getCollection("training_data");
    }

    public List<NewsItem> getCategorizedRecords(int numberOfRecords, int offset) {
        ArrayList<NewsItem> result = new ArrayList<NewsItem>();
        Iterable<NewsItem> iterable = collection().find(whereCategoryIsNotNull()).limit(numberOfRecords).skip(offset).as(NewsItem.class);
        for (NewsItem NewsItem : iterable) {
            result.add(NewsItem);
        }
        return result;
    }

    public long getCategorizedRecordsCount() {
        return collection().count(whereCategoryIsNotNull());
    }

    private String whereCategoryIsNotNull() {
        return "{'category' : {$ne : null}}";
    }

    public long count(String category) {
        return collection().count("{'category' : '" + category + "'}");
    }

    public List<Link> uniqueLinks() {
        Iterable<Link> iterable = jongo.getCollection("news_items").find("{},{\"link\" : 1, \"_id\" : 0}").as(Link.class);
        List<Link> links = new ArrayList<Link>();
        for (Link s : iterable) {
            links.add(s);
        }
        return links;
    }

    public void save(NewsItem newsItem) {
        collection().save(newsItem);
    }
}
