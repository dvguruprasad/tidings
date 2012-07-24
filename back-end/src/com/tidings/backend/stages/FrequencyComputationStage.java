package com.tidings.backend.stages;

import com.tidings.backend.domain.CategorizedWordsMatrix;
import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class FrequencyComputationStage extends Stage {

    private final CategoryDistributionRepository repository;

    public FrequencyComputationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
        repository = new CategoryDistributionRepository();
    }

    public void onMessage(Message message) {
        Document document = (Document) message.payload();
        CategorizedWordsMatrix matrix = new CategorizedWordsMatrix();
        matrix.train(document);
        repository.saveOrUpdate(matrix.distributions());
    }
}