package com.tidings.backend.domain;

import com.tidings.backend.repository.TrainingRepository;

public class Probability {
    private TrainingRepository repository;

    public Probability(TrainingRepository repository) {
        this.repository = repository;
    }

    public float ofADocumentBelongingToCategory(String category) {
        long numberOfDocumentsBelongingToCategory = repository.count(category);
        long total = repository.getCategorizedRecordsCount();
        return numberOfDocumentsBelongingToCategory / (float) total;
    }
}
