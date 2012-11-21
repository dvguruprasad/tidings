package com.tidings.backend.repository;

import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsTrainingRepository extends TrainingRepository {
    NewsTrainingRepository(String collectionName) {
        super(collectionName);
    }

    public List<Link> uniqueLinks() {
        Iterable<Link> iterable = collection().find("{},{\"link\" : 1, \"_id\" : 0}").as(Link.class);
        List<Link> links = new ArrayList<Link>();
        for (Link s : iterable) {
            links.add(s);
        }
        return links;
    }

    public void save(NewsItem newsItem) {
        collection().save(newsItem);
    }

    public boolean exists(String link) {
        return collection().count("{\"link\" : \"" + link + "\"}") != 0;
    }
}
