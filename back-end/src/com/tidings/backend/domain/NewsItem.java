package com.tidings.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

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

    @JsonIgnore
    private String fullText;

    @JsonIgnore
    private WordBag wordBag;

    public NewsItem(String title, String link, String rawText, String fullText, Date publishedDate) {
        this.title = title;
        this.link = link;
        this.rawText = rawText;
        this.fullText = fullText;
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
            this.transformedText = transformer.transform(rawText);
            this.wordBag = WordBag.create(transformer.sanitize(fullText));
            status = ItemStatus.TRANSFORMED;
        } catch (BoilerpipeProcessingException e) {
            status = ItemStatus.FAILED_TRANSFORMATION;
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

    public static enum ItemStatus {
        TRANSFORMED, FAILED_TRANSFORMATION, RAW
    }
}