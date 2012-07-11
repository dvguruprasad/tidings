package com.tidings.backend;

import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class LoadingStage extends Stage {
    public LoadingStage(Channel<Message> transformInbox, Channel<Message> loaderInbox, Fiber worker) {
        super(transformInbox, loaderInbox, worker);
    }

    public void onMessage(Message message) {
        System.out.println("message = " + message);
    }
}
