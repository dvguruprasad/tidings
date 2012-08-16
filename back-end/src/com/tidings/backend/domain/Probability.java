package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.TrainingRepository;

import java.util.Set;

public class Probability {
    private TrainingRepository repository;
    private CategoryDistributionRepository categoryDistributionRepository;

    public Probability(TrainingRepository repository, CategoryDistributionRepository categoryDistributionRepository) {
        this.repository = repository;
        this.categoryDistributionRepository = categoryDistributionRepository;
    }

    public float ofDocumentBelongingToCategory(String category) {
        long numberOfDocumentsBelongingToCategory = repository.count(category);
        long total = repository.getCategorizedRecordsCount();
        return numberOfDocumentsBelongingToCategory / (float) total;
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

    private float probabilityOfDocumentBelongingToCategoryGivenWord(Category category, CategoryDistribution distribution) {
        float documentProbability = ofDocumentBelongingToCategory(category.name());
        float categoryProbability = distribution.categoryScore(category.name()).probability();
        return (categoryProbability * documentProbability) / ((categoryProbability * documentProbability) + ((1 - categoryProbability) * (1 - documentProbability)));
    }
}
