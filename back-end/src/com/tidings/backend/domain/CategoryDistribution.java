package com.tidings.backend.domain;

import java.util.HashMap;
import java.util.Map;

public class CategoryDistribution {
    private String word;
    Map<String, CategoryScore> categoryScores;

    public CategoryDistribution() {
    }

    public CategoryDistribution(String word) {
        this.word = word;
        categoryScores = new HashMap<String, CategoryScore>();
    }

    public String word() {
        return word;
    }

    public void addOrUpdateCategory(String category) {
        addOrUpdateCategory(category, 1);
    }

    public void addOrUpdateCategory(String category, int count) {
        if (categoryScores.containsKey(category)) {
            CategoryScore categoryScore = categoryScores.get(category);
            categoryScore.addToFrequencyCount(count);
            categoryScores.put(category, categoryScore);
        } else {
            categoryScores.put(category, new CategoryScore(count));
        }
    }

    public CategoryScore categoryScore(String category) {
        return categoryScores.containsKey(category) ? categoryScores.get(category) : CategoryScore.EMPTY;
    }

    public Map<String, CategoryScore> scores() {
        return categoryScores;
    }
}