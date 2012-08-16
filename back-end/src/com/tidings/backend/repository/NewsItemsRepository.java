package com.tidings.backend.repository;

import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsItem;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class NewsItemsRepository extends Repository {

    @Override
    protected MongoCollection collection() {
        return jongo.getCollection("news_items");
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
