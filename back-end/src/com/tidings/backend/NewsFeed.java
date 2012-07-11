package com.tidings.backend;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsFeed {
    private SyndFeed feed;
    private List<NewsItem> newsItems;
    private String feedUrl;

    //these constructors suck. Will fix it later
    NewsFeed(SyndFeed syndFeed, List<NewsItem> items) {
        this.feed = syndFeed;
        this.newsItems = items;
    }

    NewsFeed(SyndFeed syndFeed) {
        this.feed = syndFeed;
        this.newsItems = extractNewsItems();
    }

    private List<NewsItem> extractNewsItems() {
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        List entries = feed.getEntries();
        for (Iterator i = entries.iterator(); i.hasNext();) {
            SyndEntry entry = (SyndEntry) i.next();
            NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), entry.getDescription().getValue());
            newsItems.add(newsItem);
        }
        return newsItems;
    }

    public static NewsFeed pull(String url) {
        try {
            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            NewsFeed newsFeed = new NewsFeed(syndFeed);
            newsFeed.feedUrl = url;
            return newsFeed;
        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return null;
    }

    public String title() {
        return feed.getTitle();
    }

    public String feedUrl() {
        return feedUrl;
    }

    public List<NewsItem> newsItems() {
        return newsItems;
    }

    public int entryCount() {
        return newsItems.size();
    }
}