package com.tidings.backend;

import java.util.HashMap;
import java.util.Map;

public class CategoryDistribution {
    private String word;
    Map<String, Integer> categoryScores;

    public String word() {
        return word;
    }

    public CategoryDistribution() {
    }

    public CategoryDistribution(String word) {
        this.word = word;
        categoryScores = new HashMap<String, Integer>();
    }


    public void addOrUpdateCategory(String category) {
        if (categoryScores.containsKey(category)) {
            categoryScores.put(category, new Integer(categoryScores.get(category).intValue() + 1));
        } else {
            categoryScores.put(category, 1);
        }
    }

    public int wordFrequency(String category) {
        return categoryScores.containsKey(category) ? categoryScores.get(category) : 0;
    }

    public Map<String, Integer> scores() {
        return categoryScores;
    }
}