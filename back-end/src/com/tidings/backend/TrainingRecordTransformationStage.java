package com.tidings.backend;

import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TrainingRecordTransformationStage extends Stage {

    public TrainingRecordTransformationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker) {
        super(inbox, outbox, worker);
    }

    public void onMessage(Message message) {

        publish(new Message("some new message"));
    }
}
