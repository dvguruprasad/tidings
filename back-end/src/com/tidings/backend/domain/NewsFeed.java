package com.tidings.backend.domain;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsFeed {
    private SyndFeed feed;
    private List<NewsItem> newsItems;

    NewsFeed(SyndFeed syndFeed) {
        this.feed = syndFeed;
        this.newsItems = extractNewsItems();
    }

    private List<NewsItem> extractNewsItems() {
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        List entries = feed.getEntries();
        for (Iterator i = entries.iterator(); i.hasNext(); ) {
            SyndEntry entry = (SyndEntry) i.next();
            if (null != entry) {
//                NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), entry.getDescription().getValue(), "", entry.getPublishedDate());
                NewsItem newsItem = new NewsItem(entry.getTitle(), entry.getLink(), entry.getDescription().getValue(), extractFullText(entry), entry.getPublishedDate());
                newsItems.add(newsItem);
            }
        }
        return newsItems;
    }

    private String extractFullText(SyndEntry entry) {
        String text = null;
        try {
            text = ArticleExtractor.INSTANCE.getText(new URL(entry.getLink()));
        } catch (BoilerpipeProcessingException e) {
            System.out.println("failed extracting content from: " + entry.getLink());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static NewsFeed pull(String url) {
        try {
            SyndFeed syndicatedFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            return new NewsFeed(syndicatedFeed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
}