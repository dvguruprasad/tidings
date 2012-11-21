package com.tidings.backend.domain;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class NewsItem extends Document {
    private String title;
    private String link;
    private Date publishedDate;

    public NewsItem() {
    }

    public NewsItem(long id, String title, String link, Date publishedDate) {
        this.identifier = id;
        this.title = title;
        this.link = link;
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "transformedText='" + transformedText + '\'' +
                '}';
    }

    public void transformUsing(NewsTransformer transformer) {
        try {
            this.transformedText = transformer.transform(fullText);
            this.wordBag = WordBag.create(transformer.sanitize(fullText));
        } catch (BoilerpipeProcessingException e) {
        }
    }

    public void transformUsing(TweetSanitizer sanitizer) {
        this.title = "foo";
        this.link = "dummy";
        this.transformedText = fullText;
        this.wordBag = WordBag.create(sanitizer.sanitize(fullText));
    }

    public void extractFullText() {
        try {
            fullText = ArticleExtractor.INSTANCE.getText(new URL(link));
        } catch (BoilerpipeProcessingException e) {
            System.out.println("failed extracting content from: " + link);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Link link() {
        return new Link(link);
    }
}