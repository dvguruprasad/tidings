package com.tidings.backend.repository;

import com.tidings.backend.domain.Link;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedsRepository extends Repository {
    protected MongoCollection collection() {
        return jongo.getCollection("news_feeds");
    }

    public List<Link> all() {
        ArrayList<Link> result = new ArrayList<Link>();
        for (Link link : collection().find().as(Link.class)) {
            result.add(link);
        }
        return result;
    }

    public long count() {
        return collection().count();
    }

    public List<Link> all(int pageNumber, int feedsPerPage) {
        List<Link> result = new ArrayList<Link>();
        Iterable<Link> iterable = collection().find().limit(feedsPerPage).skip(pageNumber * feedsPerPage).as(Link.class);
        for (Link link : iterable) {
            result.add(link);
        }
        return result;
    }
}
