package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;

import java.util.HashMap;
import java.util.Set;

public class Probability {
    private CategoryRepository categoryRepository;
    private CategoryDistributionRepository categoryDistributionRepository;
    private final HashMap<String, Long> wordFrequencies;

    public Probability(CategoryRepository categoryRepository, CategoryDistributionRepository categoryDistributionRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryDistributionRepository = categoryDistributionRepository;
        wordFrequencies = new HashMap<String, Long>();
        Iterable<Category> all = categoryRepository.all();
        for (Category category : all) {
            wordFrequencies.put(category.name(), new Long(categoryDistributionRepository.count(category.name())));
        }
    }

    public double ofDocumentBelongingToCategoryGivenWords(Category category, WordBag wordBag) {
        double result = 1;
        Set<String> words = wordBag.words();
        for (String word : words) {
            CategoryDistribution distribution = categoryDistributionRepository.findByWord(word);
            if (null != distribution) {
                double probability = probabilityOfDocumentBelongingToCategoryGivenWord(category, distribution);
                if (0.0 == probability)
                    probability = 0.05f;
                result *= probability;
            }
        }
        return result * ofDocumentBelongingToCategory(category.name());
    }

    public double ofWordAppearingInCategory(String categoryName, CategoryScore categoryScore) {
        return categoryScore.frequency() / (double) wordFrequencies.get(categoryName);
    }

    private double probabilityOfDocumentBelongingToCategoryGivenWord(Category category, CategoryDistribution distribution) {
        return distribution.categoryScore(category.name()).probability();
    }

    private double ofDocumentBelongingToCategory(String categoryName) {
        long numberOfWordsInCategory = categoryDistributionRepository.count(categoryName);
        long totalNumberOfWords = categoryDistributionRepository.count();
        return numberOfWordsInCategory / (double) totalNumberOfWords;
    }
}
