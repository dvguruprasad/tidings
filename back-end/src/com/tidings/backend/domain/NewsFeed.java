package com.tidings.backend.domain;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsFeed {
    private SyndFeed feed;
    private List<NewsItem> newsItems;

    public NewsFeed(SyndFeed syndFeed) {
        this.feed = syndFeed;
        this.newsItems = extractNewsItems();
    }

    private List<NewsItem> extractNewsItems() {
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        List entries = feed.getEntries();
        for (Iterator i = entries.iterator(); i.hasNext();) {
            SyndEntry entry = (SyndEntry) i.next();
            if (null != entry) {
                String description = entry.getDescription() == null ? "" : entry.getDescription().getValue();
                NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), description, entry.getPublishedDate());
                newsItems.add(newsItem);
            }
        }

        return newsItems;
    }


    public String title() {
        return feed.getTitle();
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