package com.tidings.backend.domain;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StopWords {
    private Set<String> words = new HashSet<String>();

    public StopWords(String... words) {
        for (String word : words) {
            this.words.add(word);
        }
    }

    public StopWords(String file) {
        try {
            List<String> strings = FileUtils.readLines(new File(file));
            for (String string : strings) {
                this.words.add(string);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(String string) {
        return words.contains(string);
    }

    public Set<String> get() {
        return words;
    }
}
