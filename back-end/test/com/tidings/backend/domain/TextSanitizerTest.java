package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TextSanitizerTest {
    @Test
    public void shouldRemoveStopWordsFromText() {
        TextSanitizer sanitizer = TextSanitizer.create(new StopWords("data/stopwords.txt"));
        List<String> sanitized = sanitizer.sanitize("Could it be that Larry picked a " +
                "fight with CEO Mike just to" +
                " help launch some new hardware");
        Assert.assertEquals(7, sanitized.size());

        assertWords(sanitized, "larry", "picked", "fight", "ceo", "mike", "launch", "hardware");
    }

    private void assertWords(List<String> sanitized, String... expectedWords) {
        for (String expectedWord : expectedWords) {
            Assert.assertTrue(sanitized.contains(expectedWord));
        }
    }
}
