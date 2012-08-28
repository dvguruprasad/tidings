package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.WordProbability;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class ProbabilityCompuationStage extends Stage {

    private final CategoryDistributionRepository categoryDistributionRepository;
    private final CategoryRepository categoryRepository;

    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber,
                                      CategoryDistributionRepository categoryDistributionRepository, CategoryRepository categoryRepository) {
        super(inbox, outbox, threadFiber);
        this.categoryDistributionRepository = categoryDistributionRepository;
        this.categoryRepository = categoryRepository;
    }

    public void onMessage(Message message) {
        System.out.println("Computing probabilities...");
        WordProbability wordProbability = WordProbability.create(categoryDistributionRepository, categoryRepository);

        Iterable<CategoryDistribution> allCategoryDistributions = categoryDistributionRepository.all();
        for (CategoryDistribution distribution : allCategoryDistributions) {
            distribution = wordProbability.compute(distribution);
            categoryDistributionRepository.saveOrUpdate(distribution);
        }
        System.out.println("Finished training!");
    }
}
