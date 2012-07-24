package com.tidings.backend;

import com.tidings.backend.training.WordBag;

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
