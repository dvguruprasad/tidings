package com.tidings.backend;

import com.tidings.backend.repository.Link;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

import java.util.Date;

public class NewsItem {
    private String title;
    private String transformedText;
    private String rawText;
    private Date publishedDate;
    private ItemStatus status;
    private String link;

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

            this.transformedText = transformer.sanitize(rawText);
            status = ItemStatus.TRANSFORMED;
        } catch (BoilerpipeProcessingException e) {
            status = ItemStatus.FAILED_TRANSFORMATION;
        }
    }

    public Link link() {
        return new Link(link);
    }

    public static enum ItemStatus {
        TRANSFORMED, FAILED_TRANSFORMATION, RAW
    }
}
