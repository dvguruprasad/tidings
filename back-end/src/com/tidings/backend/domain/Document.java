package com.tidings.backend.domain;

public class Document {
    private WordBag wordBag;
    private String category;

    public Document(WordBag wordBag, String category) {
        this.wordBag = wordBag;
        this.category = category;
    }

    public WordBag wordBag() {
        return wordBag;
    }

    public String category() {
        return category;
    }
}
