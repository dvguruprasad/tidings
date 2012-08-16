package com.tidings.backend.domain;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.net.URL;

public class NewsFeedBuilder {
    private NewsFeed instance;

    public NewsFeedBuilder pullContents(String url) {
        try {
            SyndFeed syndicatedFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            instance = new NewsFeed(syndicatedFeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public NewsFeedBuilder categorize(String category) {
        instance.categorize(category);
        return this;
    }

    public NewsFeedBuilder extractFullText() {
        instance.extractFullText();
        return this;
    }


    public NewsFeed instance() {
        return instance;
    }
}
