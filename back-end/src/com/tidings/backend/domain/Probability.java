package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;

import java.util.Set;

public class Probability {
    private CategoryRepository categoryRepository;
    private CategoryDistributionRepository categoryDistributionRepository;

    public Probability(CategoryRepository categoryRepository, CategoryDistributionRepository categoryDistributionRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryDistributionRepository = categoryDistributionRepository;
    }

    public float ofDocumentBelongingToCategoryGivenWords(Category category, WordBag wordBag) {
        float result = 1;
        Set<String> words = wordBag.words();
        for (String word : words) {
            CategoryDistribution distribution = categoryDistributionRepository.findByWord(word);
            if (null != distribution) {
                float probability = probabilityOfDocumentBelongingToCategoryGivenWord(category, distribution);
                if (0.0 == probability)
                    probability = 0.05f;
                result *= probability;
            }
        }
        return result;
    }

    public float ofWordAppearingInCategory(String categoryName, CategoryScore categoryScore) {
        return categoryScore.frequency() / (float) categoryDistributionRepository.count(categoryName);
    }

    private float probabilityOfDocumentBelongingToCategoryGivenWord(Category category, CategoryDistribution distribution) {
        float documentProbability = ofDocumentBelongingToCategory(category.name());
        float categoryProbability = distribution.categoryScore(category.name()).probability();
        return (categoryProbability * documentProbability);
    }

    private float ofDocumentBelongingToCategory(String categoryName) {
        long numberOfWordsInCategory = categoryDistributionRepository.count(categoryName);
        long totalNumberOfWords = categoryDistributionRepository.count();
        return numberOfWordsInCategory / (float) totalNumberOfWords;
    }
}
