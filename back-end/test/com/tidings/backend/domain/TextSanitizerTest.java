package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TextSanitizerTest {
    @Test
    public void shouldRemoveStopWordsFromText() {
        TextSanitizer sanitizer = TextSanitizer.create();
        List<String> sanitized = sanitizer.sanitize("Could it be that Larry picked a " +
                "fight with CEO Mike just to" +
                " help launch some new hardware");
        Assert.assertEquals(9, sanitized.size());

        assertWords(sanitized, "larry", "picked", "fight", "ceo", "mike", "help", "launch", "new", "hardware");
    }

    private void assertWords(List<String> sanitized, String... expectedWords) {
        for (String expectedWord : expectedWords) {
            Assert.assertTrue(sanitized.contains(expectedWord));
        }
    }

    private StopWords stopWords() {
        return new StopWords("a", "could", "it", "be", "that", "with", "just", "to", "some");
    }
}
