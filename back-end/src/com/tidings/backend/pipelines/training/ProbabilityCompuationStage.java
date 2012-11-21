package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.WordProbability;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.ArrayList;
import java.util.Date;

public class ProbabilityCompuationStage extends Stage {

    private final CategoryDistributionRepository categoryDistributionRepository;
    private final CategoryRepository categoryRepository;
    private final ArrayList<CategoryDistribution> categoryDistributions;

    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber,
                                      CategoryDistributionRepository categoryDistributionRepository, CategoryRepository categoryRepository) {
        super(inbox, outbox, threadFiber);
        this.categoryDistributionRepository = categoryDistributionRepository;
        this.categoryRepository = categoryRepository;
        categoryDistributions = new ArrayList<CategoryDistribution>();
    }

    public void onMessage(Message message) {
        System.out.println("Computing probabilities... ");
        long then = System.currentTimeMillis();
        WordProbability wordProbability = WordProbability.create(categoryDistributionRepository, categoryRepository);

        Iterable<CategoryDistribution> allCategoryDistributions = categoryDistributionRepository.all();
        for (CategoryDistribution distribution : allCategoryDistributions) {
            distribution = wordProbability.compute(distribution);
            categoryDistributionRepository.saveOrUpdate(distribution);
        }

        System.out.println("Finished computing probabilities. Took " + (System.currentTimeMillis() - then) + " ms.");
        System.out.println("Finished Training at " + new Date());
    }
}
