package com.tidings.backend.domain;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DocumentProbability {
    private CategoryDistributionRepository categoryDistributionRepository;
    private HashMap<String, Long> wordFrequencies;
    private final TrainingRepository trainingRepository;

    private HashMap<Category, Double> documentProbabilities;
    private final CategoryRepository categoryRepository;

    public DocumentProbability(CategoryDistributionRepository categoryDistributionRepository, TrainingRepository trainingRepository, CategoryRepository categoryRepository) {
        this.categoryDistributionRepository = categoryDistributionRepository;
        this.trainingRepository = trainingRepository;
        this.categoryRepository = categoryRepository;
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
        return result + ofDocumentBelongingToCategory(category);
    }

    private double probabilityOfDocumentBelongingToCategoryGivenWord(Category category, CategoryDistribution distribution) {
        return log(distribution.categoryScore(category.name()).probability());
    }

    private double ofDocumentBelongingToCategory(Category category) {
        if (null == documentProbabilities) {
            documentProbabilities = new HashMap<Category, Double>();
            List<Category> categories = categoryRepository.all();
            for (Category c : categories) {
                long numberOfDocumentsInCategory = trainingRepository.count(c);
                long totalNumberOfDocuments = trainingRepository.count();
                documentProbabilities.put(c, log(numberOfDocumentsInCategory / (double) totalNumberOfDocuments));
            }
        }
        return documentProbabilities.get(category);
    }

    private double log(double value) {
        return Math.abs(Math.log(value));
    }
}
