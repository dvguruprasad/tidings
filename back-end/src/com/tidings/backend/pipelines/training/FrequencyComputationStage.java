package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryDistributions;
import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FrequencyComputationStage extends Stage {
    private final CategoryDistributionRepository distributionRepository;

    private long totalTrainingRecords;
    private long totalProcessed = 0;
    private final CategoryRepository categoryRepository;
    private final CategoryDistributions categoryDistributions;

    Map<String, Long> wordCountsByCategory;

    public FrequencyComputationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, CategoryRepository categoryRepository, TrainingRepository trainingRepository, CategoryDistributionRepository distributionRepository) {
        super(inbox, outbox, threadFiber);
        this.categoryRepository = categoryRepository;
        this.distributionRepository = distributionRepository;
        totalTrainingRecords = trainingRepository.getCategorizedRecordsCount();
        categoryDistributions = new CategoryDistributions(categoryRepository.all());
        wordCountsByCategory = new HashMap<String, Long>();
    }

    public void onMessage(Message message) {
        Document document = (Document) message.payload();
        categoryDistributions.addOrUpdate(document);
        addToWordCount(document.category(), document.wordBag().count());

        totalProcessed += 1;

        if (totalProcessed == totalTrainingRecords) {
            for (CategoryDistribution categoryDistribution : categoryDistributions.list()) {
                distributionRepository.save(categoryDistribution);
                for (String category : wordCountsByCategory.keySet()) {
                    categoryRepository.addToWordCount(category, wordCountsByCategory.get(category));
                }
            }
            System.out.println("Finished frequency computing distribution. Triggering probability computation stage at " + new Date());
            publish(new Message("TriggerProbabilityComputation"));
        }
    }

    private void addToWordCount(String category, long count) {
        if (wordCountsByCategory.containsKey(category))
            wordCountsByCategory.put(category, wordCountsByCategory.get(category) + 1);
        else
            wordCountsByCategory.put(category, count);
    }
}