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

    public void addOrUpdateCategory(String category, int frequency) {
        if (categoryScores.containsKey(category)) {
            CategoryScore categoryScore = categoryScores.get(category);
            categoryScore.updateFrequency(frequency);
            categoryScores.put(category, categoryScore);
        } else {
            categoryScores.put(category, new CategoryScore(frequency));
        }
    }

    public CategoryScore categoryScore(String category) {
        return categoryScores.containsKey(category) ? categoryScores.get(category) : CategoryScore.EMPTY;
    }

    public void computeProbabilities(Probability probability) {
        int totalFrequency = totalFrequency();
        for (String category : categoryScores.keySet()) {
            computeProbability(category, totalFrequency, probability);
        }
    }

    private void computeProbability(String category, int totalFrequency, Probability probability) {
        CategoryScore categoryScore = categoryScores.get(category);
        float probabilityOfWordAppearingInCategory = categoryScore.frequency() / (float) totalFrequency;
        float documentProbability = probability.ofADocumentBelongingToCategory(category);
        float result = (probabilityOfWordAppearingInCategory * documentProbability)
                / (probabilityOfWordAppearingInCategory * documentProbability + ((1 - probabilityOfWordAppearingInCategory) * (1 - documentProbability)));
        categoryScores.get(category).setProbability(result);
    }

    private int totalFrequency() {
        int result = 0;
        for (CategoryScore categoryScore : categoryScores.values()) {
            result += categoryScore.frequency();
        }
        return result;
    }
}