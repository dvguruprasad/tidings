package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.TrainingRepository;

import java.util.HashMap;
import java.util.Set;

public class DocumentProbability {
    private CategoryDistributionRepository categoryDistributionRepository;
    private HashMap<String, Long> wordFrequencies;
    private final TrainingRepository trainingRepository;

    public DocumentProbability(CategoryDistributionRepository categoryDistributionRepository, TrainingRepository trainingRepository) {
        this.categoryDistributionRepository = categoryDistributionRepository;
        this.trainingRepository = trainingRepository;
    }

    public double compute(Category category, WordBag wordBag) {
        double result = 0;
        Set<String> words = wordBag.words();
        for (String word : words) {
            CategoryDistribution distribution = categoryDistributionRepository.findByWord(word);
            if (null != distribution) {
                result += (probabilityOfDocumentBelongingToCategoryGivenWord(category, distribution) * wordBag.frequency(word));
            }
        }
        return result + ofDocumentBelongingToCategory(category.name());
    }

    private double probabilityOfDocumentBelongingToCategoryGivenWord(Category category, CategoryDistribution distribution) {
        return log(distribution.categoryScore(category.name()).probability());
    }

    private double ofDocumentBelongingToCategory(String categoryName) {
        long numberOfDocumentsInCategory = trainingRepository.count(categoryName);
        long totalNumberOfDocuments = trainingRepository.count();
        return log(numberOfDocumentsInCategory / (double) totalNumberOfDocuments);
    }

    private double log(double value) {
        return Math.abs(Math.log(value));
    }
}
