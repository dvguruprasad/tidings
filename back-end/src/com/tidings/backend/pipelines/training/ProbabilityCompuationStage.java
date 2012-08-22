package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.Probability;
import com.tidings.backend.repository.CategoryDistributionRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class ProbabilityCompuationStage extends Stage {

    private final CategoryDistributionRepository categoryDistributionRepository;
    private Probability probability;

    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, Probability probability) {
        super(inbox, outbox, threadFiber);
        this.probability = probability;
        categoryDistributionRepository = new CategoryDistributionRepository();
    }

    public void onMessage(Message message) {
        System.out.println("Computing probabilities...");
        Iterable<CategoryDistribution> allCategoryDistributions = categoryDistributionRepository.all();
        for (CategoryDistribution distribution : allCategoryDistributions) {
            distribution.computeProbabilities(probability);
            categoryDistributionRepository.saveOrUpdate(distribution);
        }
        System.out.println("Finished training!");
    }
}
