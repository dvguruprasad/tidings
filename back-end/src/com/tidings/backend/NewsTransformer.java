package com.tidings.backend;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class NewsTransformer {
    private ArticleExtractor articleExtractor;

    public NewsTransformer() {
        articleExtractor = ArticleExtractor.getInstance();
    }

    public NewsFeed transform(NewsFeed newsFeed) {
        for (NewsItem item : newsFeed.newsItems()) {
            item.transformUsing(this);
        }
        return newsFeed;
    }

    public String sanitize(String rawText) throws BoilerpipeProcessingException {
        return articleExtractor.getText(rawText);
    }
}
