package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CategorizedWordsMatrix {
    Map<String, CategoryDistribution> distributions;
    private CategoryDistributionRepository distributionRepository;

    public CategorizedWordsMatrix() {
        distributions = new HashMap<String, CategoryDistribution>();
    }

    public CategorizedWordsMatrix(CategoryDistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
        this.distributions = new HashMap<String, CategoryDistribution>();
    }

    public CategorizedWordsMatrix train(Document document) {
        Set<String> words = document.wordBag().words();
        for (String word : words) {
            CategoryDistribution distribution = distributionRepository.findByWord(word);
            if (null == distribution) {
                distribution = new CategoryDistribution(word);
                distribution.addOrUpdateCategory(document.category());
            } else {
                distribution.addOrUpdateCategory(document.category(), document.wordBag().frequency(word));
            }
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