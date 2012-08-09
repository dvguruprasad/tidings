package com.tidings.backend.stages;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class ProbabilityCompuationStage extends Stage {

    private final CategoryDistributionRepository categoryDistributionRepository;
    private final TrainingRepository trainingRepository;

    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
        categoryDistributionRepository = new CategoryDistributionRepository();
        trainingRepository = new TrainingRepository();
    }

    public void onMessage(Message message) {
        Iterable<CategoryDistribution> allCategoryDistributions = categoryDistributionRepository.all();
        for (CategoryDistribution distribution : allCategoryDistributions) {
            distribution.computeProbabilities();
            categoryDistributionRepository.saveOrUpdate(distribution);
        }
    }
}
