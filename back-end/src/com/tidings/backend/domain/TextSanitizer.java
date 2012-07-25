package com.tidings.backend.domain;

import java.util.ArrayList;
import java.util.List;

public class TextSanitizer {
    private StopWords stopWords;

    public TextSanitizer(StopWords stopWords) {
        this.stopWords = stopWords;
    }

    public List<String> sanitize(String text) {
        ArrayList<String> result = new ArrayList<String>();
        String[] strings = text.split("\\b");
        for (String string : strings) {
            String lowerCasedString = string.toLowerCase().trim();
            if (!lowerCasedString.isEmpty() && lowerCasedString.length() > 1 && !stopWords.contains(lowerCasedString))
                result.add(lowerCasedString);
        }
        return result;
    }
}
