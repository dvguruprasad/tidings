package com.tidings.backend.pipelines.training;

import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TweetDataExtractionStage extends Stage {
    public TweetDataExtractionStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
    }

    public void onMessage(Message message) {
    }
}
