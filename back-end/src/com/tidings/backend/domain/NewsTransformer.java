package com.tidings.backend.domain;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.util.List;

public class NewsTransformer {
    private ArticleExtractor articleExtractor;
    private TextSanitizer textSanitizer;

    public NewsTransformer() {
        articleExtractor = ArticleExtractor.getInstance();
        textSanitizer = TextSanitizer.create();
    }

    public NewsFeed transform(NewsFeed newsFeed) {
        for (NewsItem item : newsFeed.newsItems()) {
            item.transformUsing(this);
        }
        return newsFeed;
    }

    public String transform(String rawText) throws BoilerpipeProcessingException {
        return articleExtractor.getText(rawText);
    }

    public List<String> sanitize(String rawText) throws BoilerpipeProcessingException {
        return textSanitizer.sanitize(rawText);
    }
}
