package com.tidings.backend;

import de.l3s.boilerpipe.BoilerpipeProcessingException;

public class NewsItem {
    private String title;
    private String transformedText;
    private String rawText;
    private ItemStatus status;
    private String link;

    public NewsItem(String title, String link, String rawText) {
        this.title = title;
        this.link = link;
        this.rawText = rawText;
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

    public ItemStatus getStatus() {
        return status;
    }

    public String getTransformedText() {
        return transformedText;
    }

    public String link() {
        return link;
    }

    public String title() {
        return title;
    }

    public static enum ItemStatus {
        TRANSFORMED, FAILED_TRANSFORMATION, RAW
    }
}
