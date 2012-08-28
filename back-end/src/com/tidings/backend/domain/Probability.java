package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;

import java.util.HashMap;
import java.util.Set;

public class Probability {
    private CategoryRepository categoryRepository;
    private CategoryDistributionRepository categoryDistributionRepository;
    private HashMap<String, Long> wordFrequencies;
    private final TrainingRepository trainingRepository;

    public Probability(CategoryRepository categoryRepository, CategoryDistributionRepository categoryDistributionRepository, TrainingRepository trainingRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryDistributionRepository = categoryDistributionRepository;
        this.trainingRepository = trainingRepository;
    }

    public double ofWordAppearingInCategory(String categoryName, CategoryScore categoryScore) {
        return (categoryScore.frequency() + 1) / ((double) wordFrequencies().get(categoryName) + totalDistinctWords());
    }

    public double ofDocumentBelongingToCategoryGivenWords(Category category, WordBag wordBag) {
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

    private HashMap<String, Long> wordFrequencies() {
        if (null == wordFrequencies) {
            wordFrequencies = new HashMap<String, Long>();
            Iterable<Category> all = categoryRepository.all();

            for (Category category : all) {
                wordFrequencies.put(category.name(), categoryRepository.find(category.name()).wordFrequency());
            }
        }
        return wordFrequencies;
    }

    private long totalDistinctWords() {
        return categoryDistributionRepository.count();
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
