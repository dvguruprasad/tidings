package com.tidings.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Document {
    protected long identifier;
    protected String fullText;

    @JsonIgnore
    protected WordBag wordBag;

    protected String category;
    protected String transformedText;

    public Document() {
    }

    public Document(long id, String fullText, String category) {
        this.identifier = id;
        this.fullText = fullText;
        this.category = category;
    }

    public String fullText() {
        return fullText;
    }

    public WordBag wordBag() {
        return wordBag;
    }

    public String category() {
        return category;
    }

    public long id() {
        return identifier;
    }

    public void sanitize(Sanitizer sanitizer) {
        List<String> sanitized = sanitizer.sanitize(fullText);
        this.wordBag = WordBag.create(sanitized);
    }


    public void categorize(String category) {
        this.category = category;
    }
}