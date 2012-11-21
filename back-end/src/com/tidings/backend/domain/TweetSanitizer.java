package com.tidings.backend.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetSanitizer implements Sanitizer {
    private StopWords stopWords;
    private Emoticons emoticons;

    private static Pattern wordPattern = Pattern.compile("\\w+'*\\w+");

    public static TweetSanitizer create(StopWords stopWords, Emoticons emoticons) {
        return new TweetSanitizer(stopWords, emoticons);
    }

    private TweetSanitizer(StopWords stopWords, Emoticons emoticons) {
        this.stopWords = stopWords;
        this.emoticons = emoticons;
    }

    public List<String> sanitize(String text) {
        if (null == text || text.isEmpty())
            return Collections.emptyList();
        List<String> result = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(text.trim());
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextElement().toString();
            if (!stopWords.contains(token)) {
                List<String> features = extractFeatures(token);
                for (String feature : features) {
                    result.add(feature);
                }
            }
        }
        return result;
    }

    private List<String> extractFeatures(String token) {
        ArrayList<String> result = new ArrayList<String>();
        if (emoticons.has(token)) {
            result.add(token);
        } else {
            Matcher matcher = wordPattern.matcher(token);
            while (matcher.find()) {
                result.add(matcher.group().toLowerCase());
            }
        }
        return result;
    }
}