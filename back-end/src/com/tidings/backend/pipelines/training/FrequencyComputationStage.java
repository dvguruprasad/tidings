package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryDistributions;
import com.tidings.backend.domain.CategoryDistributionsBuilder;
import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Date;

public class FrequencyComputationStage extends Stage {
    private final CategoryDistributionRepository distributionRepository;

    private long totalTrainingRecords;
    private long totalProcessed = 0;
    private CategoryRepository categoryRepository;
    private final CategoryDistributions categoryDistributions;

    public FrequencyComputationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, CategoryRepository categoryRepository, TrainingRepository trainingRepository) {
        super(inbox, outbox, threadFiber);
        this.categoryRepository = categoryRepository;
        distributionRepository = new CategoryDistributionRepository();
        totalTrainingRecords = trainingRepository.getCategorizedRecordsCount();
        categoryDistributions = new CategoryDistributions();
    }

    public void onMessage(Message message) {
        Document document = (Document) message.payload();
        CategoryDistributionsBuilder categoryDistributionsBuilder = new CategoryDistributionsBuilder(distributionRepository, categoryRepository);
        categoryDistributions.addOrUpdate(document);


//        List<CategoryDistribution> distributions = categoryDistributionsBuilder.distributions(document);
//        distributionRepository.saveOrUpdate(distributions, document.category());
        categoryRepository.addToWordCount(document.category(), document.wordBag().count());

        totalProcessed += 1;

        if (totalProcessed == totalTrainingRecords) {
            for (CategoryDistribution categoryDistribution : categoryDistributions.list()) {
                distributionRepository.save(categoryDistribution);
            }
            System.out.println("Triggering probability computation stage at " + new Date());
            publish(new Message("TriggerProbabilityComputation"));
        }
    }
}