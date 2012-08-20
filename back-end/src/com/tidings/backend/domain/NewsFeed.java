package com.tidings.backend.domain;

import com.sun.syndication.feed.synd.SyndFeed;

import java.util.List;

public class NewsFeed {
    private List<NewsItem> newsItems;
    private String title;

    public NewsFeed(String title, List<NewsItem> newsItems) {
        this.title = title;
        this.newsItems = newsItems;
    }

    public String title() {
        return title;
    }

    public List<NewsItem> newsItems() {
        return newsItems;
    }

    public int entryCount() {
        return newsItems.size();
    }

    public void categorize(String category) {
        for (NewsItem newsItem : newsItems) {
            newsItem.setCategory(category);
        }
    }

    public void extractFullText() {
        for (NewsItem newsItem : newsItems) {
            newsItem.extractFullText();
        }
    }
}