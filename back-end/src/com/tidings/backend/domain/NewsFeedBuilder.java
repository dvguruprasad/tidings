package com.tidings.backend.domain;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.tidings.backend.repository.TrainingRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsFeedBuilder {
    private NewsFeed instance;
    private TrainingRepository repository;

    public NewsFeedBuilder() {
    }

    public NewsFeedBuilder(TrainingRepository repository) {
        this.repository = repository;
    }

    public NewsFeedBuilder pullContents(String url) {
        try {
            SyndFeed syndicatedFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            List<NewsItem> newsItems = new ArrayList<NewsItem>();
            List entries = syndicatedFeed.getEntries();
            for (Iterator i = entries.iterator(); i.hasNext(); ) {
                SyndEntry entry = (SyndEntry) i.next();
                if (null != entry) {
                    String description = entry.getDescription() == null ? "" : entry.getDescription().getValue();
                    NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), description, entry.getPublishedDate());
                    newsItems.add(newsItem);
                }
            }

            instance = new NewsFeed(syndicatedFeed.getTitle(), newsItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public NewsFeedBuilder pullNewContents(String url) {
        try {
            SyndFeed syndicatedFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            List<NewsItem> newsItems = new ArrayList<NewsItem>();
            List entries = syndicatedFeed.getEntries();
            for (Iterator i = entries.iterator(); i.hasNext(); ) {
                SyndEntry entry = (SyndEntry) i.next();
                if (null != entry && !repository.exists(entry.getLink())) {
                    String description = entry.getDescription() == null ? "" : entry.getDescription().getValue();
                    NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), description, entry.getPublishedDate());
                    newsItems.add(newsItem);
                }
            }
            instance = new NewsFeed(syndicatedFeed.getTitle(), newsItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public NewsFeedBuilder categorize(String category) {
        if (null != instance)
            instance.categorize(category);
        return this;
    }

    public NewsFeedBuilder extractFullText() {
        if (null != instance)
            instance.extractFullText();
        return this;
    }


    public NewsFeed instance() {
        return instance;
    }
}
