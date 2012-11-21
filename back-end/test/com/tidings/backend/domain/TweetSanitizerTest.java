package com.tidings.backend.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TweetSanitizerTest {
    @Test
    public void shouldNotRemoveEmoticonsFromText() {
        TweetSanitizer sanitizer = TweetSanitizer.create(stopWords(), emoticons());
        List<String> tokens = sanitizer.sanitize("I'm starting to get really concerned, sending hashtags in emails :P #twitter is taking over our lives :D");
        System.out.println(tokens);
        
        Assert.assertEquals(14, tokens.size());
        assertWords(tokens,  "i'm", "starting", "really", "concerned", "sending", "hashtags", "emails", ":P", "twitter", "taking", "over", "our", "lives", ":D");
    }

    private void assertWords(List<String> sanitized, String... expectedWords) {
        for (String expectedWord : expectedWords) {
            Assert.assertTrue(sanitized.contains(expectedWord));
        }
    }

    private StopWords stopWords() {
        return new StopWords("a", "could", "it", "be", "that", "with", "just", "to", "some", "in", "get", "is");
    }

    private Emoticons emoticons() {
        return new Emoticons(":D", ":P");
    }
}
