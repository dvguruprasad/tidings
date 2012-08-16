package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategorizedWordsMatrix;
import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class FrequencyComputationStage extends Stage {
    private final CategoryDistributionRepository distributionRepository;
    private final TrainingRepository trainingRepository;

    private long totalTrainingRecords;
    private long totalProcessed = 0;

    public FrequencyComputationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
        distributionRepository = new CategoryDistributionRepository();
        trainingRepository = new TrainingRepository();
        totalTrainingRecords = trainingRepository.getCategorizedRecordsCount();
    }

    public void onMessage(Message message) {
        Document document = (Document) message.payload();
        Iterable<CategoryDistribution> distributions = distributionRepository.findAll(document.wordBag().words(), document.category());
        CategorizedWordsMatrix matrix = new CategorizedWordsMatrix(distributions);
        matrix.train(document);
        distributionRepository.saveOrUpdate(matrix.distributions());
        totalProcessed += 1;
        if (totalProcessed == totalTrainingRecords) {
            publish(new Message("TriggerProbabilityComputation"));
        }
    }
}