package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;

import java.util.*;

public class CategorizedWordsMatrix {
    Map<String, CategoryDistribution> distributions;
    private CategoryDistributionRepository distributionRepository;
    private List<Category> categories;

    public CategorizedWordsMatrix() {
        distributions = new HashMap<String, CategoryDistribution>();
    }

    public CategorizedWordsMatrix(CategoryDistributionRepository distributionRepository, List<Category> categories) {
        this.distributionRepository = distributionRepository;
        this.categories = categories;
        this.distributions = new HashMap<String, CategoryDistribution>();
    }

    public CategorizedWordsMatrix train(Document document) {
        Set<String> words = document.wordBag().words();
        for (String word : words) {
            CategoryDistribution distribution = distributionRepository.findByWord(word);
            if (null == distribution) {
                distribution = new CategoryDistribution(word, categories);
            }
            distribution.addOrUpdateCategory(document.category(), document.wordBag().frequency(word));
            distributions.put(distribution.word(), distribution);
        }
        return this;
    }

    public CategoryDistribution categoryDistribution(String word) {
        return distributions.get(word);
    }

    public Collection<CategoryDistribution> distributions() {
        return distributions.values();
    }
}