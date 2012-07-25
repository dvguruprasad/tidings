package com.tidings.backend.stages;

import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TrainingDataExtractionStage extends Stage {
    private TrainingRepository trainingRepository;

    public TrainingDataExtractionStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, TrainingRepository trainingRepository) {
        super(inbox, outbox, threadFiber);
        this.trainingRepository = trainingRepository;
    }

    public void onMessage(Message message) {
        publish(new Message(trainingRepository.getCategorizedRecords(418)));
    }
}
