package com.tidings.backend.domain;

public class TrainingRecord {
    private String transformedText;
    private String category;
    private String link;

    public String transformedText() {
        return transformedText;
    }

    public void setTransformedText(String transformedText) {
        this.transformedText = transformedText;
    }

    public String category() {
        return category;
    }

    public String link() {
        return link;
    }
}
