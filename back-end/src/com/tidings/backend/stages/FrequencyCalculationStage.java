package com.tidings.backend.stages;

import com.tidings.backend.CategorizedWordsMatrix;
import com.tidings.backend.Document;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.training.WordBag;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class FrequencyCalculationStage extends Stage {

    private final CategoryDistributionRepository repository;

    public FrequencyCalculationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
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