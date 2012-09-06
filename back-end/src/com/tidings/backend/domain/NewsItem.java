package com.tidings.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class NewsItem {
    private String title;
    private String transformedText;
    private Date publishedDate;
    private ItemStatus status;
    private String link;
    private String category;

    @JsonIgnore
    private String rawText;

    private String fullText;

    @JsonIgnore
    private WordBag wordBag;

    public NewsItem() {
    }

    public NewsItem(String title, String link, String rawText, Date publishedDate) {
        this.title = title;
        this.link = link;
        this.rawText = rawText;
        this.publishedDate = publishedDate;
        status = ItemStatus.RAW;
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
            this.wordBag = WordBag.create(transformer.sanitize(rawText));
            status = ItemStatus.TRANSFORMED;
        } catch (BoilerpipeProcessingException e) {
            status = ItemStatus.FAILED_TRANSFORMATION;
        }
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

    public WordBag wordBag() {
        return wordBag;
    }

    public Link link() {
        return new Link(link);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String fullText() {
        return fullText;
    }

    public String category() {
        return category;
    }

    public static enum ItemStatus {
        TRANSFORMED, FAILED_TRANSFORMATION, RAW
    }
}