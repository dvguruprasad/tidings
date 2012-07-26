package com.tidings.backend.stages;

import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryScore;
import com.tidings.backend.repository.CategoryDistributionRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Map;

public class ProbabilityCompuationStage extends Stage {

    private final CategoryDistributionRepository repository;

    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
        repository = new CategoryDistributionRepository();
    }

    public void onMessage(Message message) {
        Iterable<CategoryDistribution> allCategoryDistributions = repository.all();
        for (CategoryDistribution categoryDistribution : allCategoryDistributions) {
            Map<String, CategoryScore> scores = categoryDistribution.scores();
            for (String category : scores.keySet()) {
                scores.get(category).setProbability(calculateProbability(categoryDistribution, category));
            }
        }
    }

    private float calculateProbability(CategoryDistribution categoryDistribution, String category) {
        return 0;
    }
}
