package com.tidings.backend.domain;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StopWords {
    private List<String> words;

    public StopWords(String... words) {
        this.words = Arrays.asList(words);
    }

    public StopWords(String file) {
        try {
            words = FileUtils.readLines(new File(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean contains(String string) {
        return words.contains(string);
    }
}
