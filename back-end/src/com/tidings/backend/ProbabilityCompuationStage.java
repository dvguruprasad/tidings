package com.tidings.backend;

import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class ProbabilityCompuationStage extends Stage {
    
    public ProbabilityCompuationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
    }

    public void onMessage(Message message) {
    }
}
