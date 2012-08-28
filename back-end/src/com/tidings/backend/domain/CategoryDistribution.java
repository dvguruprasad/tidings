package com.tidings.backend.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDistribution {
    private String word;
    Map<String, CategoryScore> categoryScores;

    public CategoryDistribution() {
    }

    public CategoryDistribution(String word, List<Category> categories) {
        this.word = word;
        categoryScores = new HashMap<String, CategoryScore>();
        for (Category category : categories) {
            categoryScores.put(category.name(), new CategoryScore(0));
        }
    }

    public String word() {
        return word;
    }

    public void addOrUpdateCategory(String category) {
        addOrUpdateCategory(category, 1);
    }

    public void addOrUpdateCategory(String category, int frequency) {
        if (categoryScores.containsKey(category)) {
            CategoryScore categoryScore = categoryScores.get(category);
            categoryScore.incrementFrequency(frequency);
        } else {
            categoryScores.put(category, new CategoryScore(frequency));
        }
    }

    public CategoryScore categoryScore(String category) {
        return categoryScores.containsKey(category) ? categoryScores.get(category) : CategoryScore.EMPTY;
    }

    private int totalFrequency() {
        int result = 0;
        for (CategoryScore categoryScore : categoryScores.values()) {
            result += categoryScore.frequency();
        }
        return result;
    }

    public double probability(String category) {
        return categoryScores.containsKey(category) ? categoryScores.get(category).probability() : (double) 0.0;
    }
}