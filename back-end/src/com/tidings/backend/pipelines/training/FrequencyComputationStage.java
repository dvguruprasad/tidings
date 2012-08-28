package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategorizedWordsMatrix;
import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class FrequencyComputationStage extends Stage {
    private final CategoryDistributionRepository distributionRepository;

    private long totalTrainingRecords;
    private long totalProcessed = 0;
    private CategoryRepository categoryRepository;

    public FrequencyComputationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, CategoryRepository categoryRepository, TrainingRepository trainingRepository) {
        super(inbox, outbox, threadFiber);
        this.categoryRepository = categoryRepository;
        distributionRepository = new CategoryDistributionRepository();
        totalTrainingRecords = trainingRepository.getCategorizedRecordsCount();
    }

    public void onMessage(Message message) {
        Document document = (Document) message.payload();
        CategorizedWordsMatrix matrix = new CategorizedWordsMatrix(distributionRepository, categoryRepository.all()).train(document);
        distributionRepository.saveOrUpdate(matrix.distributions());
        categoryRepository.addToWordCount(document.category(), document.wordBag().count());

        totalProcessed += 1;
        if (totalProcessed == totalTrainingRecords) {
            System.out.println("Triggering probability computation stage");
            publish(new Message("TriggerProbabilityComputation"));
        }
    }
}