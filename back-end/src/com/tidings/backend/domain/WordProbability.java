package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;

import java.util.HashMap;
import java.util.Map;

public class WordProbability {
    private static WordProbability instance;

    private final HashMap<String, Long> totalFrequencyDistributions;
    private long totalDistinctWords;

    private WordProbability(HashMap<String, Long> totalFrequencyDistributions, long totalDistinctWords) {
        this.totalFrequencyDistributions = totalFrequencyDistributions;
        this.totalDistinctWords = totalDistinctWords;
    }

    public static WordProbability create(CategoryDistributionRepository categoryDistributionRepository, CategoryRepository categoryRepository) {
        if (null == instance) {
            instance = new WordProbability(wordFrequencies(categoryRepository), categoryDistributionRepository.count());
        }
        return instance;
    }

    public CategoryDistribution compute(CategoryDistribution distribution) {
        Map<String, CategoryScore> categoryScores = distribution.categoryScores();
        for (String category : categoryScores.keySet()) {
            double probabilityOfWordAppearingInCategory = probabilityOfWordAppearingInCategory(category, categoryScores.get(category));
            categoryScores.get(category).setProbability(probabilityOfWordAppearingInCategory);
        }
        return distribution;
    }

    private static HashMap<String, Long> wordFrequencies(CategoryRepository categoryRepository) {
        HashMap<String, Long> wordFrequencies = new HashMap<String, Long>();
        Iterable<Category> all = categoryRepository.all();
        for (Category category : all) {
            wordFrequencies.put(category.name(), category.wordFrequency());
        }
        return wordFrequencies;
    }

    private double probabilityOfWordAppearingInCategory(String categoryName, CategoryScore categoryScore) {
        return (categoryScore.frequency() + 1) / ((double) totalFrequencyDistributions.get(categoryName) + totalDistinctWords);
    }
}
